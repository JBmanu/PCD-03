import rmi.SudokuServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import static rmi.RMIPath.SERVER_NAME;
import static rmi.RMIPath.SERVER_PORT;

public final class MainServer {

    public static void main(final String[] args) {
        final Optional<SudokuServer> serverOpt = SudokuServer.create();
        serverOpt.ifPresent(server -> {
            try {
                final Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
                registry.rebind(SERVER_NAME, server.remoteObject());
                System.out.println("Sudoku RMI Server is starting...");
            } catch (final RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
