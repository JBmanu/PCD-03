package rmi;

import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import utils.Pair;
import utils.RMIUtils;
import utils.Try;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public interface SudokuServer extends Serializable, Remote {

    boolean createRoom(SudokuClient client, Settings settings) throws RemoteException;

    boolean joinRoom(SudokuClient client) throws RemoteException;

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
            if (!this.rooms.containsKey(roomId)) return Collections.emptyList(); 
            final List<SudokuClient> players = this.rooms.get(roomId).second();
            return players.stream().map(player -> Try.toOptional(player::name))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }

        @Override
        public boolean createRoom(final SudokuClient client, final Settings settings) throws RemoteException {
            client.setRoomId(this.currentId);
            final Grid grid = FactoryGrid.grid(settings);
            client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
            this.rooms.put(this.currentId, Pair.of(grid, new ArrayList<>(Collections.singleton(client))));
            this.currentId += 1;
            return true;
        }

        @Override
        public boolean joinRoom(final SudokuClient client) throws RemoteException {
            final int roomId = client.roomId();
            final String name = client.name();
            final List<String> playersName = this.playersNames(roomId);
            if (!this.rooms.containsKey(roomId) || playersName.contains(name)) return false;

            final List<SudokuClient> players = this.rooms.get(roomId).second();
            final Grid grid = this.rooms.get(roomId).first();

            for (final SudokuClient player : players) player.invokeOnJoinPlayer(name);
            client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
            client.invokeOnJoin(playersName);
            players.add(client);
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
            players.forEach(player -> Try.toOptional(player::invokeOnLeavePlayer, name));
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
            room.second().forEach(player -> Try.toOptional(player::invokeOnMove, coordinate, value));
        }

    }
}
