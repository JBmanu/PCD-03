package rmi;

import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import utils.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public interface SudokuServer extends Remote {

    SudokuClient createRoom(String namePlayer, Settings settings) throws RemoteException;

    Grid joinRoom(SudokuClient client) throws RemoteException;

    void leaveRoom(SudokuClient client) throws RemoteException;

    Grid grid(SudokuClient client) throws RemoteException;

    Grid updateCell(SudokuClient client, Coordinate coordinate, int value) throws RemoteException;


    class SudokuServerImpl extends UnicastRemoteObject implements SudokuServer {
        private final Map<Integer, Pair<List<SudokuClient>, Grid>> rooms;
        private int currentId;

        public SudokuServerImpl() throws RemoteException {
            this.rooms = new HashMap<>();
            this.currentId = 0;
        }

        @Override
        public SudokuClient createRoom(final String namePlayer, final Settings settings) throws RemoteException {
            final Optional<SudokuClient> clientOpt = FactoryRMI.client(namePlayer, this.currentId);
            if (clientOpt.isEmpty()) throw new RemoteException();
            final Grid grid = FactoryGrid.grid(settings);
            clientOpt.ifPresent(client ->
                    this.rooms.put(this.currentId, Pair.of(new ArrayList<>(Collections.singleton(client)), grid)));
            this.currentId += 1;
            return clientOpt.get();
        }

        @Override
        public Grid joinRoom(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) throw new RemoteException();
            this.rooms.get(roomId).first().add(client);
            return this.rooms.get(roomId).second();
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
        public Grid grid(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) throw new RemoteException();
            final List<SudokuClient> players = this.rooms.get(roomId).first();
            if (!players.contains(client)) throw new RemoteException();

            return this.rooms.get(roomId).second();
        }

        private List<SudokuClient> playersWithout(final SudokuClient client) {
            return this.rooms.get(client.roomId()).first().stream()
                    .filter(player -> !player.equals(client))
                    .toList();
        }

        @Override
        public Grid updateCell(final SudokuClient client, final Coordinate coordinate, final int value) throws RemoteException {
            final Grid grid = this.grid(client);
            grid.saveValue(coordinate, value);
            final List<SudokuClient> players = this.playersWithout(client);
            players.forEach(player -> {
                try {
                    player.retrieveGrid(grid);
                } catch (final RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            return grid;
        }

    }
}
