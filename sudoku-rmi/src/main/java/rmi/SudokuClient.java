package rmi;

import grid.Grid;
import grid.Settings;
import utils.Try;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.Optional;

public interface SudokuClient extends Remote, Serializable {

    String name() throws RemoteException;

    int roomId() throws RemoteException;

    void retrieveGrid(Grid grid) throws RemoteException;

    void setId(int id) throws RemoteException;

    void copyId(final SudokuClient client) throws RemoteException;


    class SudokuClientImpl extends UnicastRemoteObject implements SudokuClient {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String name;
        private int roomId;

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
        public void retrieveGrid(final Grid grid) throws RemoteException {

        }

        @Override
        public void setId(final int id) throws RemoteException {
            this.roomId = id;
        }

        @Override
        public void copyId(final SudokuClient client) throws RemoteException {
            this.setId(client.roomId());
        }

    }
}
