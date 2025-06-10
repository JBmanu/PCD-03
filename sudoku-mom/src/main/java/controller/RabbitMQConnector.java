package controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import grid.Grid;
import model.Player;
import utils.GameConsumers.DeliveryAction;
import utils.GameConsumers.GridData;
import utils.GameConsumers.PlayerAction;
import utils.Messages;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static utils.Messages.JSON_PROPERTIES;

public interface RabbitMQConnector {

    static RabbitMQConnector create() {
        return new RabbitMQConnectorImpl();
    }

    void createRoom(Player player);

    void deleteRoom(Player player);

    void deleteQueue(Player player);

    void createRoomAndJoin(Player player);

    void joinRoom(Player player);

    void sendMove(RabbitMQDiscovery discovery, Player player, Coordinate coordinate, int value);

    void receiveMove(Player player, PlayerAction action);

    void requestGrid(RabbitMQDiscovery discovery, Player player);

    void receiveRequestAndSendGrid(Player player, Grid grid);

    void receiveGrid(Player player, GridData gridData);

    // manca togliere il bind quando il giocatore esce

    class RabbitMQConnectorImpl implements RabbitMQConnector {
        private static final String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";
        private static final String EXCHANGE_TYPE = "direct";

        private final Channel channel;

        public RabbitMQConnectorImpl() {
            final ConnectionFactory connectionFactory = new ConnectionFactory();
            try {
                connectionFactory.setUri(URI);
                final Connection connection = connectionFactory.newConnection();
                this.channel = connection.createChannel();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create RabbitMQ channel: " + e.getMessage(), e);
            }
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
        public void deleteQueue(final Player player) {
            player.queue().ifPresent(queue -> {
                try {
                    this.channel.queueDelete(queue);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void createRoomAndJoin(final Player player) {
            this.createRoom(player);
            this.joinRoom(player);
        }

        @Override
        public void joinRoom(final Player player) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.queueDeclare(queue, true, false, false, null);
                    this.channel.queueBind(queue, room, name);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void sendMessage(final String room, final String routingKey, final String message) {
            try {
                final byte[] body = message.getBytes(StandardCharsets.UTF_8);
                this.channel.basicPublish(room, routingKey, JSON_PROPERTIES, body);
            } catch (final IOException e) {
                throw new RuntimeException("Failed to send message: " + e.getMessage(), e);
            }
        }

        private void receiveMessage(final String queue, final DeliveryAction action) {
            try {
                this.channel.basicConsume(queue, false, (_, delivery) -> {
                    try {
                        action.accept(delivery);
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
        }

        @Override
        public void sendMove(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate,
                             final int value) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room).stream()
                        .filter(routingKey -> !routingKey.equals(name))
                        .toList();

                routingKeys.forEach(routingKey -> {
                    final String message = Messages.ToSend.move(name, coordinate, value);
                    this.sendMessage(room, routingKey, message);
                });
            });
        }

        @Override
        public void receiveMove(final Player player, final PlayerAction action) {
            player.callActionOnData((_, queue, _) ->
                    this.receiveMessage(queue, delivery -> Messages.ToReceive.acceptMove(delivery, action)));
        }

        @Override
        public void requestGrid(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room).stream()
                        .filter(routingKey -> !routingKey.equals(name))
                        .toList();

                routingKeys.stream().findFirst().ifPresent(routingKey ->
                        this.sendMessage(room, routingKey, Messages.ToSend.requestGrid(name)));
            });
        }

        @Override
        public void receiveRequestAndSendGrid(final Player player, final Grid grid) {
            player.callActionOnData((room, queue, _) ->
                    this.receiveMessage(queue, delivery ->
                            Messages.ToReceive.acceptGridRequest(delivery, playerName ->
                                    this.sendMessage(room, playerName, Messages.ToSend.grid(grid)))));
        }

        @Override
        public void receiveGrid(final Player player, final GridData gridData) {
            player.callActionOnData((_, queue, _) ->
                    this.receiveMessage(queue, delivery ->
                            Messages.ToReceive.acceptGrid(delivery, gridData)));
        }

    }
}
