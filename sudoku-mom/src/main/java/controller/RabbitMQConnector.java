package controller;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import grid.Grid;
import model.Player;
import utils.GameConsumers.PlayerAction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static utils.Topics.REQUEST;
import static utils.Topics.REQUEST_GRID;

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

    void receiveAndSendGrid(Player player, Grid grid);

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

        @Override
        public void sendMove(final RabbitMQDiscovery discovery, final Player player, final Coordinate coordinate,
                             final int value) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room).stream()
                        .filter(routingKey -> !routingKey.equals(name))
                        .toList();

                routingKeys.forEach(routingKey -> {
                    // create json message with player name, coordinate, and value
                    // For example: {"player": "Player1", "coordinate": {"x": 0, "y": 0}, "value": 1}
                    final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                            .contentType("application/json")
                            .build();
                    // Convert the message to JSON format using gson library
                    final Gson gson = new Gson();
                    final String json = gson.toJson(
                            Map.of(
                                    "player", player.name().orElse("Unknown"),
                                    "coordinate", Map.of("row", coordinate.row(), "column", coordinate.col()),
                                    "value", value
                            )
                    );
                    try {
                        this.channel.basicPublish(room, routingKey, properties, json.getBytes(StandardCharsets.UTF_8));
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

        public void receiveMove(final Player player, final PlayerAction action) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.basicConsume(queue, false, (_, delivery) -> {
                        try {

                            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                            final Gson gson = new Gson();
                            final Map<String, Object> data = gson.fromJson(message, Map.class);
                            final String playerName = (String) data.get("player");
                            final Map<String, Object> coordinate = (Map<String, Object>) data.get("coordinate");
                            final int row = (int) ((double) coordinate.get("row"));
                            final int column = (int) ((double) coordinate.get("column"));
                            final int value = (int) ((double) data.get("value"));
                            action.accept(playerName, Coordinate.create(row, column), value);

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

        @Override
        public void requestGrid(final RabbitMQDiscovery discovery, final Player player) {
            player.callActionOnData((room, _, name) -> {
                final List<String> routingKeys = discovery.routingKeysFromBindsExchange(room).stream()
                        .filter(routingKey -> !routingKey.equals(name))
                        .toList();

                routingKeys.stream().findFirst().ifPresent(routingKey -> {
                    try {
                        // create json message with request for grid and name of the player
                        final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                                .contentType("application/json")
                                .build();
                        final String json = new Gson().toJson(Map.of(REQUEST, REQUEST_GRID, "player", name));

                        this.channel.basicPublish(room, routingKey, properties, json.getBytes(StandardCharsets.UTF_8));
                    } catch (final IOException e) {
                        throw new RuntimeException("Failed to consume messages from routing key: " + e.getMessage(), e);
                    }
                });
            });
        }

        @Override
        public void receiveAndSendGrid(final Player player, final Grid grid) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.basicConsume(queue, false, (_, delivery) -> {
                        try {
                            final byte[][] solution = grid.solutionArray();
                            final byte[][] gridArray = grid.cellsArray();

                            final Gson gson = new Gson();
                            final String json = gson
                                    .toJson(Map.of("solution", solution, "grid", gridArray));

                            final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                                    .contentType("application/json")
                                    .build();

                            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                            final Map<String, Object> data = gson.fromJson(message, Map.class);
                            final String request = (String) data.get(REQUEST);
                            if (!REQUEST_GRID.equals(request)) {
                                throw new RuntimeException("Received unexpected request: " + request);
                            }
                            final String playerName = (String) data.get("player");
                            // Publish the grid data to the room with the player's name as the routing key
                            // Note: The routing key is the player's name, so the message will be sent to the player's queue

                            this.channel.basicPublish(room, playerName, properties, json.getBytes(StandardCharsets.UTF_8));
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
