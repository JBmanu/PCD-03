package controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import grid.Grid;
import model.Player;
import utils.GameConsumers.GridData;
import utils.GameConsumers.JoinPlayer;
import utils.GameConsumers.LeavePlayer;
import utils.GameConsumers.PlayerMove;
import utils.Messages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

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

    void activeCallbackReceiveMessage(Player player, Grid grid,
                                      JoinPlayer joinPlayer, LeavePlayer leavePlayer,
                                      PlayerMove moveAction, GridData gridData);


    class RabbitMQConnectorImpl implements RabbitMQConnector {
        private static final String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";
        private static final String EXCHANGE_TYPE = "direct";

        private final Channel channel;

        public RabbitMQConnectorImpl() throws URISyntaxException, NoSuchAlgorithmException,
                KeyManagementException, IOException, TimeoutException {
            final ConnectionFactory connectionFactory = new ConnectionFactory();
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
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.queueDeclare(queue, true, false, false, null);
                    this.channel.queueBind(queue, room, name);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void joinRoom(final RabbitMQDiscovery discovery, final Player player) {
            this.onlyJoinRoom(player);
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room, name);
                routingKeys.forEach(routingKey ->
                        this.sendMessage(room, routingKey, Messages.ToSend.joinPlayer(name)));
            });
        }

        @Override
        public void leaveRoom(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.queueUnbind(queue, room, name);
                    discovery.routingKeysFromBindsExchange(room, name)
                            .forEach(routingKey ->
                                    this.sendMessage(room, routingKey, Messages.ToSend.leavePlayer(name)));

                    if (discovery.countExchangeBinds(room) == 0) this.channel.exchangeDelete(room);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void sendMessage(final String room, final String routingKey, final String message) {
            try {
                final byte[] body = message.getBytes(StandardCharsets.UTF_8);
                this.channel.basicPublish(room, routingKey, JSON_PROPERTIES, body);
                //request response
            } catch (final IOException e) {
                throw new RuntimeException("Failed to send message: " + e.getMessage(), e);
            }
        }

        @Override
        public void sendGridRequest(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room, name);
                routingKeys.stream().findFirst().ifPresent(routingKey ->
                        this.sendMessage(room, routingKey, Messages.ToSend.gridRequest(name)));
            });
        }

        @Override
        public void sendMove(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate,
                             final int value) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room, name);
                routingKeys.forEach(routingKey -> {
                    final String message = Messages.ToSend.move(name, coordinate, value);
                    this.sendMessage(room, routingKey, message);
                });
            });
        }

        @Override
        public void activeCallbackReceiveMessage(final Player player, final Grid grid,
                                                 final JoinPlayer joinPlayer, final LeavePlayer leavePlayer,
                                                 final PlayerMove moveAction, final GridData gridData) {
            player.callActionOnData((room, queue, _) -> {
                try {
                    this.channel.basicConsume(queue, false, (_, delivery) -> {
                        try {
                            final Map<String, Object> message = Messages.ToReceive.createMessage(delivery);

                            switch (message.get(TYPE_MESSAGE_KEY).toString()) {
                                case Messages.TYPE_GRID_REQUEST ->
                                        Messages.ToReceive.acceptGridRequest(delivery, playerName ->
                                                this.sendMessage(room, playerName, Messages.ToSend.grid(grid)));
                                case Messages.TYPE_JOIN_PLAYER ->
                                        Messages.ToReceive.acceptJoinPlayer(delivery, joinPlayer);
                                case Messages.TYPE_LEAVE_PLAYER ->
                                        Messages.ToReceive.acceptLeavePlayer(delivery, leavePlayer);
                                case Messages.TYPE_GRID -> Messages.ToReceive.acceptGrid(delivery, gridData);
                                case Messages.TYPE_MOVE -> Messages.ToReceive.acceptMove(delivery, moveAction);
                            }
                            this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (final IOException e) {
                            this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                            throw new RuntimeException("Failed to acknowledge message: " + e.getMessage(), e);
                        }
                    }, _ -> {
                    });
                } catch (final IOException e) {
                    throw new RuntimeException("Failed to consume messages from queue: " + e.getMessage(), e);
                }
            });
        }

    }
}
