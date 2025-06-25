package rmi;

import grid.Coordinate;
import rmi.CallbackClient.CallbackJoinPlayers;
import rmi.CallbackClient.CallbackLeavePlayer;
import rmi.CallbackClient.CallbackMove;
import utils.Try;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface SudokuClient extends Serializable, Remote {

    String name() throws RemoteException;

    int roomId() throws RemoteException;

    void setRoomId(int roomId) throws RemoteException;

    void invokeMove(Coordinate coordinate, int value) throws RemoteException;

    void invokeJoinPlayer(String player) throws RemoteException;

    void invokeLeavePlayer(String player) throws RemoteException;


    class SudokuClientImpl extends UnicastRemoteObject implements SudokuClient {
        @Serial
        private static final long serialVersionUID = 1L;

        private int roomId;
        private final String name;
        private final CallbackMove callbackMove;
        private final CallbackJoinPlayers callbackPlayers;
        private final CallbackLeavePlayer callbackLeavePlayer;

        public SudokuClientImpl(final String name,
                                final CallbackMove callbackMove,
                                final CallbackJoinPlayers callbackPlayers,
                                final CallbackLeavePlayer callbackLeavePlayer) throws RemoteException {
            this.name = name;
            this.roomId = -1;
            this.callbackMove = callbackMove;
            this.callbackPlayers = callbackPlayers;
            this.callbackLeavePlayer = callbackLeavePlayer;
        }

        @Override
        public String name() throws RemoteException {
            return this.name;
        }

        @Override
        public int roomId() throws RemoteException {
            return this.roomId;
        }

        @Override
        public void setRoomId(final int roomId) throws RemoteException {
            this.roomId = roomId;
        }

        @Override
        public void invokeMove(final Coordinate coordinate, final int value) throws RemoteException {
            Try.toOptional(this.callbackMove::accept, coordinate, value);
        }

        @Override
        public void invokeJoinPlayer(final String player) throws RemoteException {
            Try.toOptional(this.callbackPlayers::accept, player);
        }

        @Override
        public void invokeLeavePlayer(final String player) throws RemoteException {
            Try.toOptional(this.callbackLeavePlayer::accept, player);
        }

    }
}
