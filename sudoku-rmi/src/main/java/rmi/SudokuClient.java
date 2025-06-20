package rmi;

import grid.Coordinate;
import rmi.ClientConsumers.CallbackMove;
import utils.Try;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface SudokuClient extends Remote, Serializable {

    String name() throws RemoteException;

    int roomId() throws RemoteException;
    
    void setCallbackMove(CallbackMove callbackMove) throws RemoteException;
    
    void invokeMove(Coordinate coordinate, int value) throws RemoteException;


    class SudokuClientImpl extends UnicastRemoteObject implements SudokuClient {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int roomId;
        private CallbackMove callbackMove;

        public SudokuClientImpl(final String name, final int roomId) throws RemoteException {
            this.name = name;
            this.roomId = roomId;
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
        public void setCallbackMove(final CallbackMove callbackMove) throws RemoteException {
            this.callbackMove = callbackMove;
        }

        @Override
        public void invokeMove(final Coordinate coordinate, final int value) throws RemoteException {
            Try.toOptional(this.callbackMove::accept, coordinate, value);
        }

    }
}
