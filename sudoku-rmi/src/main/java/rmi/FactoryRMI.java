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

    static SudokuClient createClient(final String name,
                                     final CallbackClient.CallbackMove callbackMove,
                                     final CallbackClient.CallbackJoinPlayers callbackPlayers,
                                     final CallbackClient.CallbackLeavePlayer callbackLeavePlayer) throws RemoteException {
        return new SudokuClient.SudokuClientImpl(name, callbackMove, callbackPlayers, callbackLeavePlayer);
    }

    static SudokuClient registerClient(final SudokuClient client) throws RemoteException, AlreadyBoundException {
        final Registry registry = LocateRegistry.getRegistry(SERVER_PORT);
        registry.rebind(generateRoomName(client.name(), client.roomId()), client);
        return client;
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

    static SudokuServer createAndRegisterServer() throws RemoteException, AlreadyBoundException {
        final Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
        final SudokuServer server = new SudokuServer.SudokuServerImpl();
        registry.rebind(SERVER_NAME, server);
        return server;
    }

    static void shutdownServer() throws RemoteException, NotBoundException {
        final Registry registry = LocateRegistry.getRegistry();
        registry.unbind(SERVER_NAME);
    }

    static void shutdownClient(final String name, final int roomId) throws RemoteException, NotBoundException {
        final Registry registry = LocateRegistry.getRegistry(SERVER_PORT);
        registry.unbind(generateRoomName(name, roomId));
    }
}
