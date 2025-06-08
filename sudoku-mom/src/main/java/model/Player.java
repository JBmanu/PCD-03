package model;

import utils.GameConsumers.PlayerData;

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

    void computeToCreateRoom(String countRoom, String countQueue, String playerName);

    void computeToJoinRoom(String roomId, String countQueue, String playerName);

    Optional<String> computeRoomID();

    String computeRoomNameFrom(String roomId);

    void callActionOnData(PlayerData action);


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
        public void computeToCreateRoom(final String countRoom, final String countQueue, final String playerName) {
            final String roomName = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom));
            final String queueName = String.join(DIVISOR, List.of(roomName, QUEUE, countQueue, PLAYER, playerName));
            this.room = Optional.of(roomName);
            this.queue = Optional.of(queueName);
            this.name = Optional.of(playerName);
        }

        @Override
        public void computeToJoinRoom(final String roomId, final String countQueue, final String playerName) {
            final String roomName = this.computeRoomNameFrom(roomId);
            final String queueName = String.join(DIVISOR, List.of(roomName, QUEUE, countQueue, PLAYER, playerName));
            this.room = Optional.of(roomName);
            this.queue = Optional.of(queueName);
            this.name = Optional.of(playerName);
        }
        
        @Override
        public Optional<String> computeRoomID() {
            return this.room.map(name -> name.replaceAll(DOMAIN + "\\" + DIVISOR, "")
                    .replaceAll("\\" + DIVISOR, ""));
        }

        @Override
        public String computeRoomNameFrom(final String roomId) {
            return String.join(DIVISOR,
                    List.of(DOMAIN, ROOM, roomId.replaceAll(ROOM, "")));
        }

        @Override
        public void callActionOnData(final PlayerData action) {
            this.room.ifPresent(room ->
                    this.queue.ifPresent(queue ->
                            this.name.ifPresent(name ->
                                    action.accept(room, queue, name))));
        }

        @Override
        public String toString() {
            return this.queue.orElse("");
        }
    }
}
