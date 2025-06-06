package rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public interface RabbitMQConnector {

    static RabbitMQConnector create() {
        return new RabbitMQConnectorImpl();
    }

    void createPlayer(String queueName);

    void createRoom(final String roomName, final String queueName, final String playerName);

    void deleteRoom(String roomName);



    class RabbitMQConnectorImpl implements RabbitMQConnector {
        private static final String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";
        private static final String EXCHANGE_TYPE = "direct";

        private final Channel channel;

        public RabbitMQConnectorImpl() {
            final ConnectionFactory connectionFactory = new ConnectionFactory();

            try {
                connectionFactory.setUri(URI);
                this.channel = connectionFactory.newConnection().createChannel();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create RabbitMQ channel: " + e.getMessage(), e);
            }
        }

        @Override
        public void createPlayer(final String queueName) {
            try {
                this.channel.queueDeclare(queueName, true, false, false, null);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void createRoom(final String roomName, final String queueName, final String playerName) {
            try {
                this.channel.exchangeDeclare(roomName, EXCHANGE_TYPE, true);
                this.channel.queueBind(queueName, roomName, playerName);
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

    }


}
