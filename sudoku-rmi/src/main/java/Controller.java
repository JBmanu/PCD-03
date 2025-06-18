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
import java.util.function.Consumer;

import static utils.RMIPath.*;

public class Controller implements GameMultiplayerListener.PlayerListener {
    public static final String CONNECTED_TO_SERVER = "Connected to server";
    public static final String SERVER_IS_NOT_AVAILABLE = "Server is not available";
    public static final String PLAYER_NAME_IS_REQUIRED = "Player name is required";

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
                this.server = Optional.of(server);
                this.ui.showInfo(CONNECTED_TO_SERVER);
            } catch (final RemoteException | NotBoundException _) {
            }
        } while (this.server.isEmpty());
    }


    private void callServer(final Consumer<SudokuServer> consumer) {
        this.server.ifPresentOrElse(
                consumer,
                () -> this.ui.showError(SERVER_IS_NOT_AVAILABLE));
    }

    private void createRoom(final String playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {
//        this.callServer(server -> {
//            try {
//                this.client = server.createRoom(playerName, FactoryGrid.settings(schema, difficulty));
//                final Optional<Grid> grid = this.client.flatMap(client -> TryOptional.tryOptional(server::grid, client));
//                this.client.map(SudokuClient::roomId).ifPresent(id -> this.ui.buildRoom(id + ""));
//                grid.ifPresent(this.ui::buildGrid);
//                this.ui.showGridPage();
//            } catch (final RemoteException e) {
//                System.out.println("Failed to create room: " + e.getMessage());
//            }
//        });
    }

    private void joinRoom(final String roomId, final String playerName) {
        this.callServer(server -> {

        });
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
        this.callServer(_ -> playerName.ifPresentOrElse(
                name -> room.ifPresentOrElse(
                        roomCode -> this.joinRoom(roomCode, name),
                        () -> this.createRoom(name, schema, difficulty)),
                () -> this.ui.showError(PLAYER_NAME_IS_REQUIRED)));
    }

    @Override
    public void onExit() {

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


}
