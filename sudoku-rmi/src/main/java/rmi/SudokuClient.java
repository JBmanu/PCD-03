package rmi;

import grid.Coordinate;
import rmi.CallbackClient.*;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;

public interface SudokuClient extends Serializable, Remote {

    int roomId() throws RemoteException;

    String name() throws RemoteException;

    void setRoomId(int roomId) throws RemoteException;

    void setName(String name) throws RemoteException;

    void invokeOnEnter(byte[][] solution, byte[][] cells) throws RemoteException;

    void invokeOnMove(Coordinate coordinate, int value) throws RemoteException;

    void invokeOnJoin(List<String> players) throws RemoteException;

    void invokeOnJoinPlayer(String player) throws RemoteException;

    void invokeOnLeavePlayer(String player) throws RemoteException;


    class SudokuClientImpl extends UnicastRemoteObject implements SudokuClient {
        @Serial
        private static final long serialVersionUID = 1L;

        private Optional<Integer> roomId;
        private Optional<String> name;
        private final CallbackOnMove onMove;
        private final CallbackOnJoin onJoin;
        private final CallbackOnEnter onEnter;
        private final CallbackOnJoinPlayer onJoinPlayer;
        private final CallbackLeavePlayer onLeavePlayer;

        public SudokuClientImpl(final CallbackOnEnter onEnter,
                                final CallbackOnJoin onJoin,
                                final CallbackOnMove onMove,
                                final CallbackOnJoinPlayer onJoinPlayer,
                                final CallbackLeavePlayer onLeavePlayer) throws RemoteException {
            this.name = Optional.empty();
            this.roomId = Optional.empty();
            this.onMove = onMove;
            this.onJoin = onJoin;
            this.onEnter = onEnter;
            this.onJoinPlayer = onJoinPlayer;
            this.onLeavePlayer = onLeavePlayer;
        }

        @Override
        public String name() throws RemoteException {
            return this.name.orElse("Unknown");
        }

        @Override
        public int roomId() throws RemoteException {
            return this.roomId.orElse(-1);
        }

        @Override
        public void setRoomId(final int roomId) throws RemoteException {
            this.roomId = Optional.of(roomId);
        }

        @Override
        public void setName(final String name) throws RemoteException {
            this.name = Optional.of(name);
        }

        @Override
        public void invokeOnEnter(final byte[][] solution, final byte[][] cells) throws RemoteException {
            this.onEnter.accept(solution, cells);
        }

        @Override
        public void invokeOnMove(final Coordinate coordinate, final int value) throws RemoteException {
            this.onMove.accept(coordinate, value);
        }

        @Override
        public void invokeOnJoin(final List<String> players) throws RemoteException {
            this.onJoin.accept(players);
        }

        @Override
        public void invokeOnJoinPlayer(final String player) throws RemoteException {
            this.onJoinPlayer.accept(player);
        }

        @Override
        public void invokeOnLeavePlayer(final String player) throws RemoteException {
            this.onLeavePlayer.accept(player);
        }

    }
}
