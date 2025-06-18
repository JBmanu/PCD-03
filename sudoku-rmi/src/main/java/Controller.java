import grid.Coordinate;
import grid.Settings;
import rmi.SudokuClient;
import rmi.SudokuServer;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import static rmi.RMIPath.*;

public class Controller implements GameMultiplayerListener.PlayerListener {
    private Optional<SudokuServer> server;
    private final Optional<SudokuClient> client;
    private final UIMultiplayer ui;

    public Controller() {
        this.ui = new ViewMultiPlayer();
        this.client = Optional.empty();
        this.server = Optional.empty();

        this.ui.addPlayerListener(this);
        this.ui.open();
        
        this.loadService();
    }

    private void loadService() {
        do {
            try {
                final Registry registry = LocateRegistry.getRegistry(HOST, SERVER_PORT);
                final Remote remote = registry.lookup(SERVER_NAME);
                final SudokuServer server = ((SudokuServer) remote);
                System.out.println("Connected to server: " + server);
                this.server = Optional.of(server);
                this.ui.showInfo("Connected to server");
            } catch (final RemoteException | NotBoundException _) {
            }
        } while (this.server.isEmpty());
    }


    @Override
    public void onHome() {

    }

    @Override
    public void onUndo() {

    }

    @Override
    public void onSuggest() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        return false;
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {

    }

    @Override
    public void onExit() {

    }
}
