package rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import static utils.RMIPath.*;

public interface FactoryRMI {

    private static String generateRoomName(final String name, final int roomId) {
        return "room." + roomId + ".name." + name;
    }

    static Optional<SudokuServer> retrieveServer() {
        try {
            final Registry registry = LocateRegistry.getRegistry(HOST, SERVER_PORT);
            final Remote remote = registry.lookup(SERVER_NAME);
            return Optional.of((SudokuServer) remote);
        } catch (final RemoteException | NotBoundException e) {
            System.out.println("Failed to retrieve SudokuServer: " + e.getMessage());
            return Optional.empty();
        }
    }

    static SudokuServer server() throws RemoteException, AlreadyBoundException {
        final Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
        final SudokuServer server = new SudokuServer.SudokuServerImpl();
        registry.bind(SERVER_NAME, server);
        return server;
    }


    static SudokuClient client(final String name, final int roomId) throws RemoteException, AlreadyBoundException {
        final Registry registry = LocateRegistry.getRegistry(SERVER_PORT);
        final SudokuClient client = new SudokuClient.SudokuClientImpl(name, roomId);
        registry.bind(generateRoomName(name, roomId), client);
        return client;
    }
    
}
