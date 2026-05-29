package rmi;

import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import utils.Pair;
import utils.RMITypes.ThrowingConsumers;
import utils.RMIUtils;
import utils.Try;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public interface SudokuServer extends Serializable, Remote {

    enum JoinResult { SUCCESS, ROOM_NOT_FOUND, NAME_ALREADY_TAKEN }
    
    boolean createRoom(SudokuClient client, Settings settings) throws RemoteException;

    JoinResult joinRoom(SudokuClient client) throws RemoteException;

    void leaveRoom(SudokuClient client) throws RemoteException;

    byte[][] solution(SudokuClient client) throws RemoteException;

    byte[][] grid(SudokuClient client) throws RemoteException;

    void focusGainedCell(SudokuClient client, Coordinate coordinate) throws RemoteException;

    void focusLostCell(SudokuClient client, Coordinate coordinate) throws RemoteException;

    void updateCell(SudokuClient client, Coordinate coordinate, int value) throws RemoteException;

    class SudokuServerImpl extends UnicastRemoteObject implements SudokuServer {
        private final Map<Integer, Pair<Grid, List<SudokuClient>>> rooms;
        private final Map<Integer, ReentrantLock> roomLocks;
        private int currentId;

        public SudokuServerImpl() throws RemoteException {
            this.rooms = new ConcurrentHashMap<>();
            this.roomLocks = new ConcurrentHashMap<>();
            this.currentId = 0;
        }

        private ReentrantLock lock(final int roomId) {
            return this.roomLocks.computeIfAbsent(roomId, _ -> new ReentrantLock());
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

        private void notifyOrRemove(final SudokuClient player, final int roomId,
                                    final ThrowingConsumers.Consumer<SudokuClient> action) {
            try {
                action.accept(player);
            } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
                System.out.println("Client " + Try.toOptional(player::name).orElse("unknown") + " is dead, removing...");
                this.rooms.computeIfPresent(roomId, (_, room) -> {
                    room.second().removeIf(p -> RMIUtils.comparePlayers(p, player));
                    return room.second().isEmpty() ? null : room;
                });
            }
        }

        @Override
        public synchronized boolean createRoom(final SudokuClient client, final Settings settings) throws RemoteException {
            client.setRoomId(this.currentId);
            client.setColor(RMIUtils.generateColor(this.currentId + 1));
            final Grid grid = FactoryGrid.grid(settings);
            client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
            this.roomLocks.put(this.currentId, new ReentrantLock());
            this.rooms.put(this.currentId, Pair.of(grid, new ArrayList<>(Collections.singleton(client))));
            this.currentId += 1;
            return true;
        }

        @Override
        public JoinResult joinRoom(final SudokuClient client) throws RemoteException {
            final ClientDatas clientDatas = client.datas();
            final int roomId = clientDatas.roomId();
            final ReentrantLock lock = this.lock(roomId);
            lock.lock();
            try {
                if (!this.rooms.containsKey(roomId)) return JoinResult.ROOM_NOT_FOUND;

                final List<ClientDatas> playersDatas = this.playersNames(roomId);
                if (playersDatas.stream().map(ClientDatas::name).toList().contains(clientDatas.name()))
                    return JoinResult.NAME_ALREADY_TAKEN;

                final List<SudokuClient> players = this.rooms.get(roomId).second();
                final Grid grid = this.rooms.get(roomId).first();

                client.setColor(RMIUtils.generateColor(roomId + players.size() + 1));
                final ClientDatas clientDatasWithColor = client.datas();

                for (final SudokuClient player : players)
                    this.notifyOrRemove(player, roomId, p -> p.invokeOnJoinNewPlayer(clientDatasWithColor));
                client.invokeOnEnter(grid.solutionArray(), grid.cellsArray());
                client.invokeOnJoinRoom(playersDatas);
                players.add(client);
                return JoinResult.SUCCESS;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void leaveRoom(final SudokuClient client) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final ClientDatas clientDatas = client.datas();
            final int roomId = clientDatas.roomId();
            final ReentrantLock lock = this.lock(roomId);
            lock.lock();
            try {
                final Pair<Grid, List<SudokuClient>> room = this.rooms.get(roomId);
                final List<SudokuClient> players = room.second();
                players.removeIf(player -> RMIUtils.comparePlayers(player, client));
                if (players.isEmpty()) {
                    this.rooms.remove(roomId);
                    this.roomLocks.remove(roomId);
                }
                players.forEach(player ->
                        this.notifyOrRemove(player, roomId, p -> p.invokeOnLeavePlayer(clientDatas)));
            } finally {
                lock.unlock();
            }
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
            final int roomId = clientDatas.roomId();
            final ReentrantLock lock = this.lock(roomId);
            lock.lock();
            try {
                final List<SudokuClient> withoutCaller = this.takeAllWithout(client);
                withoutCaller.forEach(player ->
                        this.notifyOrRemove(player, roomId, p -> p.invokeOnFocusGained(clientDatas, coordinate)));
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void focusLostCell(final SudokuClient client, final Coordinate coordinate) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final ClientDatas clientDatas = client.datas();
            final int roomId = clientDatas.roomId();
            final ReentrantLock lock = this.lock(roomId);
            lock.lock();
            try {
                final List<SudokuClient> withoutCaller = this.takeAllWithout(client);
                withoutCaller.forEach(player ->
                        this.notifyOrRemove(player, roomId, p -> p.invokeOnFocusLost(clientDatas, coordinate)));
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void updateCell(final SudokuClient client, final Coordinate coordinate, final int value) throws RemoteException {
            if (this.cantDoAction(client)) throw new RemoteException();
            final int roomId = client.roomId();
            final ReentrantLock lock = this.lock(roomId);
            lock.lock();
            try {
                final Pair<Grid, List<SudokuClient>> room = this.rooms.get(roomId);
                room.first().saveValue(coordinate, value);
                room.second().forEach(player ->
                        this.notifyOrRemove(player, roomId, p -> p.invokeOnMove(coordinate, value)));
            } finally {
                lock.unlock();
            }
        }
    }
}