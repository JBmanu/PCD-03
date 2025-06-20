package rmi;

import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import rmi.ServerConsumers.UpdateGrid;
import utils.Pair;
import utils.Try;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public interface SudokuServer extends Remote {

    SudokuClient createRoom(String namePlayer, Settings settings, UpdateGrid callback) throws RemoteException;

    SudokuClient joinRoom(String namePlayer, int roomId, UpdateGrid callback) throws RemoteException;

    void leaveRoom(SudokuClient client) throws RemoteException;

    byte[][] solution(SudokuClient client) throws RemoteException;

    byte[][] grid(SudokuClient client) throws RemoteException;

    void updateCell(SudokuClient client, Coordinate coordinate, int value) throws RemoteException;


    class SudokuServerImpl extends UnicastRemoteObject implements SudokuServer {
        private final Map<Integer, Pair<Grid, List<SudokuClient>>> rooms;
        private int currentId;

        public SudokuServerImpl() throws RemoteException {
            this.rooms = new HashMap<>();
            this.currentId = 0;
        }

        @Override
        public SudokuClient createRoom(final String namePlayer, final Settings settings, final ServerConsumers.UpdateGrid callback) throws RemoteException {
            final Optional<SudokuClient> clientOpt = FactoryRMI.client(namePlayer, this.currentId);
            if (clientOpt.isEmpty()) throw new RemoteException();
            final Grid grid = FactoryGrid.grid(settings);
            callback.accept(grid.solutionArray(), grid.solutionArray());
            clientOpt.ifPresent(client ->
                    this.rooms.put(this.currentId, Pair.of(grid, new ArrayList<>(Collections.singleton(client)))));
            this.currentId += 1;

            return clientOpt.get();
        }

        @Override
        public SudokuClient joinRoom(final String namePlayer, final int roomId, final UpdateGrid callback) throws RemoteException {
            if (!this.rooms.containsKey(roomId)) throw new RemoteException();
            // manca nome uguale
            final Optional<SudokuClient> clientOpt = FactoryRMI.client(namePlayer, roomId);
            if (clientOpt.isEmpty()) throw new RemoteException();
            this.rooms.get(roomId).second().add(clientOpt.get());
            final Grid grid = this.rooms.get(roomId).first();
            callback.accept(grid.solutionArray(), grid.cellsArray());
            return clientOpt.get();
        }

        @Override
        public void leaveRoom(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) return;
            final Pair<Grid, List<SudokuClient>> room = this.rooms.get(roomId);
            final List<SudokuClient> players = room.second();
            players.remove(client);
            if (players.isEmpty()) this.rooms.remove(roomId);
            // retrive players
        }

        private boolean isPlayerInRoom(final SudokuClient client) throws RemoteException {
            if (!this.rooms.containsKey(client.roomId())) return false;
            final List<String> names = this.rooms.get(client.roomId()).second().stream()
                    .map(player -> Try.toOptional(player::name))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return names.contains(client.name());
        }

        @Override
        public byte[][] solution(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) throw new RemoteException();
            if (!this.isPlayerInRoom(client)) throw new RemoteException();
            final Grid grid = this.rooms.get(roomId).first();
            return grid.solutionArray();
        }

        @Override
        public byte[][] grid(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            if (!this.rooms.containsKey(roomId)) throw new RemoteException();
            if (!this.isPlayerInRoom(client)) throw new RemoteException();
            final Grid grid = this.rooms.get(roomId).first();
            return grid.cellsArray();
        }

        private List<SudokuClient> playersWithout(final SudokuClient client) {
            final Optional<Integer> roomIdOpt = Try.toOptional(client::roomId);
            if (roomIdOpt.isEmpty() || !this.rooms.containsKey(roomIdOpt.get())) return List.of();
            return this.rooms.get(roomIdOpt.get()).second().stream()
                    .filter(player -> !player.equals(client))
                    .toList();
        }

        @Override
        public void updateCell(final SudokuClient client, final Coordinate coordinate, final int value) throws RemoteException {
//            final byte[] grid = this.grid(client, );
//            grid.saveValue(coordinate, value);
//            final List<SudokuClient> players = this.playersWithout(client);
//            players.forEach(player -> {
//                try {
//                    player.retrieveGrid(grid);
//                } catch (final RemoteException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            return grid;
        }

    }
}
