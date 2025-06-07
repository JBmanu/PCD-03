package rabbitMQ;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import player.Player;
import utils.GameConsumers.PlayerAction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public interface RabbitMQConnector {

    static RabbitMQConnector create() {
        return new RabbitMQConnectorImpl();
    }

    void createPlayerQueue(String queueName);

    void deletePlayerQueue(String queueName);

    void createRoom(String roomName);

    void deleteRoom(String roomName);

    void createRoomWithPlayer(Player player);

    void joinRoom(Player player);

    void sendMove(GameRoomQueueDiscovery discovery, Player player, Coordinate coordinate, int value);

    void receiveMove(String queue, PlayerAction action);


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
        public void createPlayerQueue(final String queueName) {
            try {
                this.channel.queueDeclare(queueName, true, false, false, null);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void deletePlayerQueue(final String queueName) {
            try {
                this.channel.queueDelete(queueName);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void createRoom(final String roomName) {
            try {
                this.channel.exchangeDeclare(roomName, EXCHANGE_TYPE, true);
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create room: " + e.getMessage(), e);
            }
        }

        @Override
        public void deleteRoom(final String roomName) {
            try {
                this.channel.exchangeDelete(roomName);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void createRoomWithPlayer(final Player player) {
            player.callActionOnData((room, queue, _) -> {
                this.createRoom(room);
                this.createPlayerQueue(queue);
                this.joinRoom(player);
            });
        }

        @Override
        public void joinRoom(final Player player) {
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.queueBind(queue, room, name);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void sendMove(final GameRoomQueueDiscovery discovery, final Player player, final Coordinate coordinate, final int value) {
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
                                    "coordinate", Map.of("row", coordinate.row(), "column", coordinate.column()),
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

        public void receiveMove(final String queueName, final PlayerAction action) {
            try {
                this.channel.basicConsume(queueName, false, (_, delivery) -> {
                    try {
                    
                        final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        final Gson gson = new Gson();
                        final Map<String, Object> data = gson.fromJson(message, Map.class);
                        final String playerName = (String) data.get("player");
                        final Map<String, Object> coordinate = (Map<String, Object>) data.get("coordinate");
                        final int row = (int)((double) coordinate.get("row"));
                        final int column = (int)((double) coordinate.get("column"));
                        final int value = (int)((double) data.get("value"));
                        action.accept(playerName, Coordinate.create(row, column), value);
                        
                        this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (final IOException e) {
                        this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        throw new RuntimeException("Failed to acknowledge message: " + e.getMessage(), e);
                    }
                }, _ -> { });
            } catch (final IOException e) {
                throw new RuntimeException("Failed to consume messages from queue: " + e.getMessage(), e);
            }
        }

    }


}
