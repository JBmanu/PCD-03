package rabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import player.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public interface RabbitMQConnector {

    static RabbitMQConnector create() {
        return new RabbitMQConnectorImpl();
    }

    void createPlayerQueue(String queueName);

    void deletePlayerQueue(String queueName);

    void createRoom(String roomName);

    void deleteRoom(String roomName);

    void createRoomWithPlayer(final Player player);

    void joinRoom(final Player player);

    void sendMove(final GameRoomQueueDiscovery discovery, Player player, Coordinate coordinate, int value);

    void receiveMessage(String queue);


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
            player.callActionOnData((room, queue, name) -> {
                try {
                    this.channel.exchangeDeclare(room, EXCHANGE_TYPE, true);
                    this.channel.queueDeclare(queue, true, false, false, null);
                    this.channel.queueBind(queue, room, name);
                } catch (final Exception e) {
                    throw new RuntimeException("Failed to create room: " + e.getMessage(), e);
                }
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
            
        }
        
        public void receiveMessage(final String queueName) {
            try {
                this.channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println("Received message: " + message);
                }, consumerTag -> { });
            } catch (final IOException e) {
                throw new RuntimeException("Failed to consume messages from queue: " + e.getMessage(), e);
            }
        }

    }


}
