package rmi;

import grid.Coordinate;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public final class CallbackClient {

    public interface CallbackMove extends Serializable, Remote {
        void accept(Coordinate coordinate, int value) throws RemoteException;
    }

    public interface CallbackJoinPlayers extends Serializable, Remote {
        void accept(String player) throws RemoteException;
    }

    public interface CallbackLeavePlayer extends Serializable, Remote {
        void accept(String player) throws RemoteException;
    }

}
