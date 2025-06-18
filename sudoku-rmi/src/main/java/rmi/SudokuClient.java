package rmi;

import grid.Grid;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuClient extends Remote, Serializable {

    int roomId();

    void retrieveGrid(Grid grid) throws RemoteException;

    SudokuClient setId(int id) throws RemoteException;

    default SudokuClient copyId(final SudokuClient client) throws RemoteException {
        return this.setId(client.roomId());
    }


    record SudokuClientImpl(String name, int roomId) implements SudokuClient {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void retrieveGrid(final Grid grid) throws RemoteException {

        }

        @Override
        public SudokuClient setId(final int id) throws RemoteException {
            return new SudokuClientImpl(this.name, id);
        }
    }
}
