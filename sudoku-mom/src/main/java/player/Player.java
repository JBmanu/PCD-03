package player;

import java.util.List;
import java.util.Optional;

public interface Player {
    String QUEUE = "queue";
    String DIVISOR = ".";

    String DOMAIN = "sudoku";
    String ROOM = "room";
    String PLAYER = "player";

    static Player create() {
        return new PlayerImpl();
    }

    Optional<String> room();

    Optional<String> queue();

    Optional<String> name();

    void computeRoom(String countRoom);

    void computeQueue(String countRoom, String countQueue, String playerName);

    void name(String name);

    Optional<String> computeRoomID();

    String convertRoomID(String roomId);

    class PlayerImpl implements Player {
        private Optional<String> room;
        private Optional<String> queue;
        private Optional<String> name;

        public PlayerImpl() {
            this.room = Optional.empty();
            this.queue = Optional.empty();
            this.name = Optional.empty();
        }

        @Override
        public Optional<String> room() {
            return this.room;
        }

        @Override
        public Optional<String> queue() {
            return this.queue;
        }

        @Override
        public Optional<String> name() {
            return this.name;
        }

        @Override
        public void computeRoom(final String countRoom) {
            final String roomName = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom));
            this.room = Optional.of(roomName);
        }

        @Override
        public void computeQueue(final String countRoom, final String countQueue, final String playerName) {
            final String queueName = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom, QUEUE, countQueue, PLAYER, playerName));
            this.queue = Optional.of(queueName);
        }

        @Override
        public void name(final String name) {
            this.name = Optional.of(name);
        }

        @Override
        public Optional<String> computeRoomID() {
            return this.room.map(name -> name.replaceAll("sudoku\\.", "").replaceAll("\\.", ""));
        }

        @Override
        public String convertRoomID(final String roomId) {
            return String.join(DIVISOR, List.of(DOMAIN, ROOM, roomId.replaceAll(ROOM, "")));
        }

    }
}
