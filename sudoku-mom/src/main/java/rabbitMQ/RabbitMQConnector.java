package rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public interface RabbitMQConnector {
    String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";
    String EXCHANGE = "exchange";
    String EXCHANGE_TYPE = "direct";

    class RabbitMQConnectorImpl implements RabbitMQConnector {
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

//        @Override
//        public void createRoom(final String countRoom, final String countQueue, final String playerName) {
////            final String routingKey = PLAYER + playerName;
////            this.room = Optional.of(roomName);
////            this.queue = Optional.of(queueName);
////            this.name = Optional.of(playerName);
//
//            try {
////                this.channel.exchangeDeclare(roomName, EXCHANGE_TYPE, true);
////                this.channel.queueDeclare(queueName, true, false, false, null);
////                this.channel.queueBind(queueName, roomName, routingKey);
//
////                System.out.println("ROOM NAME: " + roomName);
////                System.out.println("QUEUE NAME: " + queueName);
//            } catch (final Exception e) {
//                throw new RuntimeException("Failed to create room: " + e.getMessage(), e);
//            }
//        }

    }


}
