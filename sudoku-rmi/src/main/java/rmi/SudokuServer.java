package rmi;

import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import rmi.CallbackServer.CallbackGrid;
import rmi.CallbackServer.CallbackJoinPlayers;
import utils.Pair;
import utils.RMIUtils;
import utils.Try;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public interface SudokuServer extends Serializable, Remote {

    boolean createRoom(SudokuClient client, Settings settings,
                       CallbackGrid callbackGrid) throws RemoteException, AlreadyBoundException;

    boolean joinRoom(SudokuClient client,
                     CallbackGrid callbackGrid, CallbackJoinPlayers callbackJoinPlayers) throws RemoteException, AlreadyBoundException;

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

        private boolean cantDoAction(final SudokuClient client) {
            final Optional<Integer> roomIdOpt = Try.toOptional(client::roomId);
            return !roomIdOpt.map(id -> this.rooms.containsKey(id) &&
                    RMIUtils.containsPlayer(this.rooms.get(id).second(), client)).orElse(false);
        }

        private List<String> playersNames(final int roomId) throws RemoteException {
            final List<SudokuClient> players = this.rooms.get(roomId).second();
            return players.stream().map(player -> Try.toOptional(player::name))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }

        @Override
        public boolean createRoom(final SudokuClient client, final Settings settings,
                                  final CallbackGrid callbackGrid) throws RemoteException, AlreadyBoundException {
            client.setRoomId(this.currentId);
            final Grid grid = FactoryGrid.grid(settings);
            callbackGrid.accept(grid.solutionArray(), grid.solutionArray());
            this.rooms.put(this.currentId, Pair.of(grid, new ArrayList<>(Collections.singleton(client))));
            this.currentId += 1;
            return true;
        }

        @Override
        public boolean joinRoom(final SudokuClient client,
                                final CallbackGrid callbackGrid, final CallbackJoinPlayers callbackJoinPlayers) throws RemoteException, AlreadyBoundException {
            final int roomId = client.roomId();
            final List<String> playerNames = this.playersNames(roomId);
            if (!this.rooms.containsKey(roomId) || playerNames.contains(client.name())) return false;
            
            final List<SudokuClient> players = this.rooms.get(roomId).second();
            final Grid grid = this.rooms.get(roomId).first();

            for (final SudokuClient player : players) player.invokeJoinPlayer(client.name());
            callbackJoinPlayers.accept(this.playersNames(roomId));
            players.add(client);
            callbackGrid.accept(grid.solutionArray(), grid.cellsArray());
            return true;
        }

        @Override
        public void leaveRoom(final SudokuClient client) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final int roomId = client.roomId();
            final String name = client.name();
            final Pair<Grid, List<SudokuClient>> room = this.rooms.get(roomId);
            final List<SudokuClient> players = room.second();
            players.removeIf(player -> RMIUtils.comparePlayers(player, client));
            if (players.isEmpty()) this.rooms.remove(roomId);
            players.forEach(player -> Try.toOptional(player::invokeLeavePlayer, name));
        }

        @Override
        public byte[][] solution(final SudokuClient client) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final Grid grid = this.rooms.get(client.roomId()).first();
            return grid.solutionArray();
        }

        @Override
        public byte[][] grid(final SudokuClient client) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final Grid grid = this.rooms.get(client.roomId()).first();
            return grid.cellsArray();
        }

        @Override
        public void updateCell(final SudokuClient client, final Coordinate coordinate, final int value) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final Pair<Grid, List<SudokuClient>> room = this.rooms.get(client.roomId());
            room.first().saveValue(coordinate, value);
            room.second().forEach(player -> Try.toOptional(player::invokeMove, coordinate, value));
        }

    }
}
