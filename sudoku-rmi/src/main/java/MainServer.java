import rmi.FactoryRMI;
import rmi.SudokuServer;
import utils.Try;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import static utils.RMIPath.SERVER_NAME;
import static utils.RMIPath.SERVER_PORT;

public final class MainServer {

    public static void main(final String[] args) {
        Optional<SudokuServer> sudokuServer = Try.toOptional(FactoryRMI::server);
        while (sudokuServer.isEmpty()) sudokuServer = Try.toOptional(FactoryRMI::server);
        
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
