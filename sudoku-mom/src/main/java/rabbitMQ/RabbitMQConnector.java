package rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;
import player.Player;

import java.io.IOException;

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

    void sendMove(Player player, Coordinate coordinate, int value);


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
        public void sendMove(final Player player, final Coordinate coordinate, final int value) {

        }

    }


}
