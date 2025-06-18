package rmi;

import java.rmi.RemoteException;
import java.util.Optional;

public interface FactoryRMI {

    static Optional<SudokuServer> createSudokuServer() {
        try {
            return Optional.of(new SudokuServer.SudokuServerImpl());
        } catch (final RemoteException _) {
            return Optional.empty();
        }
    }


}
