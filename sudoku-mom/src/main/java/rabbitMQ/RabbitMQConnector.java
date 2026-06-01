package rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import grid.Grid;
import model.Player;
import utils.GameConsumers.*;
import utils.Messages;
import utils.Topics;

import javax.net.ssl.SSLContext;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static utils.Messages.JSON_PROPERTIES;
import static utils.Messages.TYPE_MESSAGE_KEY;

public interface RabbitMQConnector {

    static Optional<RabbitMQConnector> create() {
        try {
            return Optional.of(new RabbitMQConnectorImpl());
        } catch (final URISyntaxException | TimeoutException | IOException | KeyManagementException |
                       NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }

    void createRoom(Player player);
    void deleteRoom(Player player);
    void deleteQueue(RabbitMQDiscovery discovery, Player player);
    void createRoomAndJoin(Player player);
    void joinRoom(RabbitMQDiscovery discovery, Player player);
    void leaveRoom(RabbitMQDiscovery discovery, Player player);
    void sendGridRequest(RabbitMQDiscovery discovery, Player player);
    void sendMove(RabbitMQDiscovery discovery, Player player, Coordinate coordinate, int value);
    void sendFocusGained(RabbitMQDiscovery discovery, Player player, Coordinate coordinate);
    void sendFocusLost(RabbitMQDiscovery discovery, Player player, Coordinate coordinate);
    void activeCallbackReceiveMessage(RabbitMQDiscovery discovery, Player player, Supplier<Grid> gridSupplier,
                                      JoinPlayer joinPlayer, LeavePlayer leavePlayer,
                                      PlayerMove moveAction, CreationGrid initGrid,
                                      FocusGained focusGained, FocusLost focusLost);

    class RabbitMQConnectorImpl implements RabbitMQConnector {
        private static final String URI = "amqps://bexxolvf:QArxTTpT8a3bnUzuyVHGvajfavrdAIt7@kangaroo.rmq.cloudamqp.com/bexxolvf";
        private static final String EXCHANGE_TYPE = "fanout";

        private final Channel channel;

        public RabbitMQConnectorImpl() throws URISyntaxException, NoSuchAlgorithmException,
                KeyManagementException, IOException, TimeoutException {
            final ConnectionFactory connectionFactory = new ConnectionFactory();
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, null, null);
            connectionFactory.useSslProtocol(sslContext);
            connectionFactory.setUri(URI);
            final Connection connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
        }

        @Override
        public void createRoom(final Player player) {
            player.callActionOnData((room, _, _) -> {
                try {
                    this.channel.exchangeDeclare(room, EXCHANGE_TYPE, true);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void deleteRoom(final Player player) {
            player.room().ifPresent(room -> {
                try {
                    this.channel.exchangeDelete(room);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void deleteQueue(final RabbitMQDiscovery discovery, final Player player) {
            player.queue().ifPresent(queue -> {
                try {
                    this.leaveRoom(discovery, player);
                    this.channel.queueDelete(queue);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void createRoomAndJoin(final Player player) {
            this.createRoom(player);
            this.onlyJoinRoom(player);
        }

        private void onlyJoinRoom(final Player player) {
            player.callActionOnData((room, queue, _) -> {
                try {
                    this.channel.queueDeclare(queue, true, false, true, null);
                    this.channel.queueBind(queue, room, "");
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void sendMessage(final String room, final String message) {
            try {
                final byte[] body = message.getBytes(StandardCharsets.UTF_8);
                this.channel.basicPublish(room, "", JSON_PROPERTIES, body);
            } catch (final IOException e) {
                throw new RuntimeException("Failed to send message: " + e.getMessage(), e);
            }
        }

        @Override
        public void joinRoom(final RabbitMQDiscovery discovery, final Player player) {
            this.onlyJoinRoom(player);
            player.callActionOnData((room, _, name) ->
                    this.sendMessage(room, Messages.ToSend.joinPlayer(name, player.color().orElse(Color.black))));
        }

        @Override
        public void leaveRoom(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.queueUnbind(queue, room, "");
                    this.sendMessage(room, Messages.ToSend.leavePlayer(name));
                    if (discovery.countExchangeBinds(room) == 0) this.channel.exchangeDelete(room);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void sendGridRequest(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, _, name) ->
                    this.sendMessage(room, Messages.ToSend.gridRequest(name)));
        }

        @Override
        public void sendMove(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate, final int value) {
            player.callActionOnData((room, _, name) ->
                    this.sendMessage(room, Messages.ToSend.move(name, coordinate, value)));
        }

        @Override
        public void sendFocusGained(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate) {
            player.callActionOnData((room, _, name) ->
                    player.color().ifPresent(color ->
                            this.sendMessage(room, Messages.ToSend.focusGained(name, coordinate, color))));
        }

        @Override
        public void sendFocusLost(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate) {
            player.callActionOnData((room, _, name) ->
                    this.sendMessage(room, Messages.ToSend.focusLost(name, coordinate)));
        }

        @Override
        public void activeCallbackReceiveMessage(final RabbitMQDiscovery discovery, final Player player,
                                                 final Supplier<Grid> gridSupplier,
                                                 final JoinPlayer joinPlayer, final LeavePlayer leavePlayer,
                                                 final PlayerMove moveAction, final CreationGrid initGrid,
                                                 final FocusGained focusGained, final FocusLost focusLost) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.basicConsume(queue, false, (_, delivery) -> {
                        try {
                            final Map<String, Object> message = Messages.ToReceive.createMessage(delivery);
                            switch (message.get(TYPE_MESSAGE_KEY).toString()) {
                                case Messages.TYPE_GRID_REQUEST -> {
                                    final Grid currentGrid = gridSupplier.get();
                                    if (currentGrid != null) {
                                        final int myCount = Topics.extractCountQueueFrom(
                                                player.queue().orElse(""));
                                        final int minCount = discovery.queueNamesFromExchange(room).stream()
                                                .mapToInt(Topics::extractCountQueueFrom)
                                                .min()
                                                .orElse(myCount);
                                        if (myCount == minCount) {
                                            Messages.ToReceive.acceptGridRequest(delivery, playerName ->
                                                    this.sendMessage(room, Messages.ToSend.grid(currentGrid, playerName)));
                                        }
                                    }
                                }
                                case Messages.TYPE_JOIN_PLAYER ->
                                        Messages.ToReceive.acceptJoinPlayer(delivery, name, joinPlayer);
                                case Messages.TYPE_LEAVE_PLAYER ->
                                        Messages.ToReceive.acceptLeavePlayer(delivery, name, leavePlayer);
                                case Messages.TYPE_GRID ->
                                        Messages.ToReceive.acceptGrid(delivery, name, initGrid);
                                case Messages.TYPE_MOVE ->
                                        Messages.ToReceive.acceptMove(delivery, name, moveAction);
                                case Messages.TYPE_FOCUS_GAINED ->
                                        Messages.ToReceive.acceptFocusGained(delivery, name, focusGained);
                                case Messages.TYPE_FOCUS_LOST ->
                                        Messages.ToReceive.acceptFocusLost(delivery, name, focusLost);
                            }
                            this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (final IOException e) {
                            this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                            throw new RuntimeException("Failed to acknowledge message: " + e.getMessage(), e);
                        }
                    }, _ -> this.sendMessage(room, Messages.ToSend.leavePlayer(name)));
                } catch (final IOException e) {
                    throw new RuntimeException("Failed to consume messages from queue: " + e.getMessage(), e);
                }
            });
        }
    }
}
