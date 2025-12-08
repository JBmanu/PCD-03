package model;

import utils.GameConsumers.PlayerData;
import utils.Topics;

import java.util.Optional;

public interface Player {

    static Player create() {
        return new PlayerImpl();
    }

    Optional<String> computeRoomID();
    
    Optional<String> room();

    Optional<String> queue();

    Optional<String> name();

    void computeToCreateRoom(String countRoom, String countQueue, String playerName);

    void computeToJoinRoom(String roomId, String countQueue, String playerName);

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
        public Optional<String> computeRoomID() {
            return Topics.computeRoomIDFrom(this);
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
            this.room = Optional.of(Topics.computeRoomName(countRoom));
            this.queue = Optional.of(Topics.computePlayerQueueName(countRoom, countQueue, playerName));
            this.name = Optional.of(playerName);
        }

        @Override
        public void computeToJoinRoom(final String roomId, final String countQueue, final String playerName) {
            this.room = Optional.of(Topics.computeRoomNameFrom(roomId));
            this.queue = Optional.of(Topics.computePlayerQueueNameFrom(roomId, countQueue, playerName));
            this.name = Optional.of(playerName);
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
