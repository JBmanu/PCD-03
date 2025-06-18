import rmi.FactoryRMI;
import rmi.SudokuServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import static rmi.RMIPath.SERVER_NAME;
import static rmi.RMIPath.SERVER_PORT;

public final class MainServer {

    public static void main(final String[] args) {
        Optional<SudokuServer> sudokuServer = FactoryRMI.createSudokuServer();

        while (sudokuServer.isEmpty()) sudokuServer = FactoryRMI.createSudokuServer();
        
        sudokuServer.ifPresent(server -> {
            try {
                final Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
                registry.bind(SERVER_NAME, server);
                System.out.println("Sudoku server is running on port " + SERVER_PORT);
            } catch (final RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
