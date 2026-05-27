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

    void focusGainedCell(SudokuClient client, Coordinate coordinate) throws RemoteException;

    void focusLostCell(SudokuClient client, Coordinate coordinate) throws RemoteException;

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

        private List<ClientDatas> playersNames(final int roomId) {
            if (!this.rooms.containsKey(roomId)) return Collections.emptyList();
            final List<SudokuClient> players = this.rooms.get(roomId).second();
            return players.stream().map(player -> Try.toOptional(player::datas))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }

        @Override
        public boolean createRoom(final SudokuClient client, final Settings settings) throws RemoteException {
            client.setRoomId(this.currentId);
            client.setColor(RMIUtils.generateColor(this.currentId + 1));
            final Grid grid = FactoryGrid.grid(settings);
            client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
            this.rooms.put(this.currentId, Pair.of(grid, new ArrayList<>(Collections.singleton(client))));
            this.currentId += 1;
            return true;
        }
        
        @Override
        public boolean joinRoom(final SudokuClient client) throws RemoteException {
            final ClientDatas clientDatas = client.datas();
            final int roomId = clientDatas.roomId();
            final List<ClientDatas> playersDatas = this.playersNames(roomId);
            if (!this.rooms.containsKey(roomId) ||
                    playersDatas.stream().map(ClientDatas::name).toList().contains(clientDatas.name())) return false;

            final List<SudokuClient> players = this.rooms.get(roomId).second();
            final Grid grid = this.rooms.get(roomId).first();

            client.setColor(RMIUtils.generateColor(roomId + players.size() + 1)); // ← spostato qui
            final ClientDatas clientDatasWithColor = client.datas();               // ← ri-serializza con colore

            for (final SudokuClient player : players) player.invokeOnJoinNewPlayer(clientDatasWithColor);
            client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
            client.invokeOnJoinRoom(playersDatas);
            players.add(client);
            return true;
        }

        @Override
        public void leaveRoom(final SudokuClient client) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final ClientDatas clientDatas = client.datas();
            final Pair<Grid, List<SudokuClient>> room = this.rooms.get(clientDatas.roomId());
            final List<SudokuClient> players = room.second();
            players.removeIf(player -> RMIUtils.comparePlayers(player, client));
            if (players.isEmpty()) this.rooms.remove(clientDatas.roomId());
            players.forEach(player -> Try.toOptional(player::invokeOnLeavePlayer, clientDatas));
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

        private List<SudokuClient> takeAllWithout(final SudokuClient client) throws RemoteException {
            final ClientDatas clientDatas = client.datas();
            final Pair<Grid, List<SudokuClient>> room = this.rooms.get(clientDatas.roomId());
            return room.second().stream()
                    .filter(player -> !RMIUtils.comparePlayers(player, client))
                    .toList();
        }
        
        @Override
        public void focusGainedCell(final SudokuClient client, final Coordinate coordinate) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final ClientDatas clientDatas = client.datas();
            final List<SudokuClient> withoutCaller = this.takeAllWithout(client);
            withoutCaller.forEach(player -> Try.toOptional(player::invokeOnFocusGained, clientDatas, coordinate));
        }

        @Override
        public void focusLostCell(final SudokuClient client, final Coordinate coordinate) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final ClientDatas clientDatas = client.datas();
            final List<SudokuClient> withoutCaller = this.takeAllWithout(client);
            withoutCaller.forEach(player -> Try.toOptional(player::invokeOnFocusLost, clientDatas, coordinate));
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
