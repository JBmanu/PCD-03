package player;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import grid.Coordinate;

import java.util.List;
import java.util.Optional;

public interface Player {

    static Player create() {
        return new PlayerImpl();
    }

    void createRoom(String countRoom, String countQueue, String playerName);

    void joinRoom(String countRoom, String countQueue, String playerName);

    void leaveRoom();

    void putNumber(Coordinate coordinate, int number);


    class PlayerImpl implements Player {
        private static final String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";
        private static final String EXCHANGE_TYPE = "direct";

        public static final String EXCHANGE = "exchange";
        public static final String QUEUE = "queue";
        public static final String DIVISOR = ".";

        public static final String DOMAIN = "sudoku";
        public static final String ROOM = "room";
        public static final String PLAYER = "player";

        private Optional<String> room;
        private Optional<String> queue;
        private Optional<String> name;

        private final Channel channel;

        public PlayerImpl() {
            this.room = Optional.empty();
            this.queue = Optional.empty();
            this.name = Optional.empty();

            final ConnectionFactory connectionFactory = new ConnectionFactory();

            try {
                connectionFactory.setUri(URI);
                this.channel = connectionFactory.newConnection().createChannel();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create RabbitMQ channel: " + e.getMessage(), e);
            }
        }

        @Override
        public void createRoom(final String countRoom, final String countQueue, final String playerName) {
            final String roomName = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom));
            final String queueName = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom, QUEUE, countQueue, PLAYER, playerName));
            final String routingKey = PLAYER + playerName;
            
            this.room = Optional.of(roomName);
            this.queue = Optional.of(queueName);
            this.name = Optional.of(playerName);

            try {
                this.channel.exchangeDeclare(roomName, EXCHANGE_TYPE, true);
                this.channel.queueDeclare(queueName, true, false, false, null);
                this.channel.queueBind(queueName, roomName, routingKey);

                System.out.println("ROOM NAME: " + roomName);
                System.out.println("QUEUE NAME: " + queueName);
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create room: " + e.getMessage(), e);
            }
        }

        @Override
        public void joinRoom(final String countRoom, final String countQueue, final String playerName) {

        }

        @Override
        public void leaveRoom() {

        }

        @Override
        public void putNumber(final Coordinate coordinate, final int number) {

        }
    }


}
