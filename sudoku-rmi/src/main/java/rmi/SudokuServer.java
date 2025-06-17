package rmi;

import grid.Coordinate;
import grid.Grid;
import grid.Settings;
import utils.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public interface SudokuServer {

    static Optional<SudokuServer> create() {
        try {
            return Optional.of(new SudokuServerImpl());
        } catch (final RemoteException e) {
            return Optional.empty();
        }
    }

    UnicastRemoteObject remoteObject();

    Optional<SudokuClient> createRoom(String namePlayer, Settings settings) throws RemoteException;

    Optional<Grid> joinRoom(SudokuClient client) throws RemoteException;

    void leaveRoom(SudokuClient client) throws RemoteException;

    Optional<Grid> grid(SudokuClient client) throws RemoteException;

    Optional<Grid> updateCell(SudokuClient client, Coordinate coordinate, int value) throws RemoteException;


    class SudokuServerImpl extends UnicastRemoteObject implements SudokuServer {
        private final Map<Integer, Pair<List<SudokuClient>, Grid>> rooms;
        private int currentId;

        public SudokuServerImpl() throws RemoteException {
            this.rooms = new HashMap<>();
            this.currentId = 0;
        }

        @Override
        public UnicastRemoteObject remoteObject() {
            return this;
        }

        @Override
        public Optional<SudokuClient> createRoom(final String namePlayer, final Settings settings) throws RemoteException {
            final Grid grid = Grid.create(settings);
            final Optional<SudokuClient> clientOpt = SudokuClient.create(namePlayer, this.currentId);
            clientOpt.ifPresent(client ->
                    this.rooms.put(this.currentId, Pair.of(new ArrayList<>(Collections.singleton(client)), grid)));
            this.currentId += 1;
            return clientOpt;
        }

        @Override
        public Optional<Grid> joinRoom(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) return Optional.empty();
            this.rooms.get(roomId).first().add(client);
            return Optional.of(this.rooms.get(roomId).second());
        }

        @Override
        public void leaveRoom(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) return;
            final Pair<List<SudokuClient>, Grid> room = this.rooms.get(roomId);
            final List<SudokuClient> players = room.first();
            players.remove(client);
            if (players.isEmpty()) this.rooms.remove(roomId);
            // retrive players
        }

        @Override
        public Optional<Grid> grid(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) return Optional.empty();
            final List<SudokuClient> players = this.rooms.get(roomId).first();
            if (!players.contains(client)) return Optional.empty();
            
            return Optional.of(this.rooms.get(roomId).second());
        }

        private List<SudokuClient> playersWithout(final SudokuClient client) {
            return this.rooms.get(client.roomId()).first().stream()
                    .filter(player -> !player.equals(client))
                    .toList();
        }

        @Override
        public Optional<Grid> updateCell(final SudokuClient client, final Coordinate coordinate, final int value) throws RemoteException {
            final Optional<Grid> gridOptional = this.grid(client);
            gridOptional.ifPresent(grid -> {
                grid.saveValue(coordinate, value);
                final List<SudokuClient> players = this.playersWithout(client);
                players.forEach(player -> player.retrieveGrid(grid));
            });
            return gridOptional;
        }

    }
}
