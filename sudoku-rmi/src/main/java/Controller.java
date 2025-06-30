import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import rmi.CallbackClient;
import rmi.FactoryRMI;
import rmi.SudokuClient;
import rmi.SudokuServer;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;
import utils.Consumers;
import utils.Try;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Controller implements Serializable, GameMultiplayerListener.PlayerListener, CallbackClient.Callbacks {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String CONNECTED_TO_SERVER = "Connected to server";
    public static final String SERVER_IS_NOT_AVAILABLE = "Server is not available";
    public static final String CLIENT_IS_NOT_AVAILABLE = "Client is not available";

    public static final String PLAYER_NAME_IS_REQUIRED = "Player name is required";

    private Optional<SudokuServer> server;
    private Optional<SudokuClient> client;
    private final UIMultiplayer ui;
    private Grid grid;

    public Controller() {
        this.ui = new ViewMultiPlayer();
        this.client = Try.toOptional(FactoryRMI::createClient, this, this, this, this, this);
        this.server = FactoryRMI.retrieveServer();

        this.ui.addPlayerListener(this);
        this.ui.open();

        this.loadService();
    }

    private void loadService() {
        while (this.server.isEmpty()) {
            this.ui.showInfo(CONNECTED_TO_SERVER);
            this.server = FactoryRMI.retrieveServer();
        }

        while (this.client.isEmpty()) {
            this.client = Try.toOptional(FactoryRMI::createClient, this, this, this, this, this);
        }
    }

    private void callServerAndClient(final Consumers.BiConsumer<SudokuServer, SudokuClient> consumer) {
        this.server.ifPresentOrElse(
                server -> this.client.ifPresentOrElse(client -> consumer.accept(server, client),
                        () -> this.ui.showError(CLIENT_IS_NOT_AVAILABLE)),
                () -> this.ui.showError(SERVER_IS_NOT_AVAILABLE));
    }

    @Override
    public void callbackOnEnter(final byte[][] solution, final byte[][] cells) {
        this.grid = FactoryGrid.gridLoad(solution, cells);
        this.ui.buildGrid(this.grid);
        this.ui.showGridPage();
    }

    @Override
    public void callbackOnJoin(final List<String> players) {
        this.ui.appendPlayers(players);
    }

    @Override
    public void callbackOnJoinPlayer(final String player) {
        this.ui.joinPlayer(player);
    }

    @Override
    public void callbackOnMove(final Coordinate coordinate, final int value) {
        if (value == this.grid.emptyValue()) this.grid.undo();
        else this.grid.saveValue(coordinate, value);
        this.ui.writeValueWithoutCheck(coordinate, value);
        this.checkWin();
    }

    @Override
    public void callbackOnLeavePlayer(final String player) {
        this.ui.leavePlayer(player);
    }

    private void createRoom(final SudokuServer server, final SudokuClient client, final String playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {
        Try.toOptional(client::setName, playerName);
        Try.toOptional(server::createRoom, client, FactoryGrid.settings(schema, difficulty));
        Try.toOptional(FactoryRMI::registerClient, client);
        final Optional<Integer> id = Try.toOptional(client::roomId);
        id.ifPresent(roomId -> this.ui.buildRoom(roomId + ""));
    }

    private void joinRoom(final SudokuServer server, final SudokuClient client, final String roomId, final String playerName) {
        Try.toOptional(client::setName, playerName);
        Try.toOptional(client::setRoomId, Integer.parseInt(roomId));
        Try.toOptional(FactoryRMI::registerClient, client);
        this.ui.buildRoom(roomId);
        Try.toOptional(server::joinRoom, client);
    }

    private void checkWin() {
        if (this.grid.hasWin()) {
            this.callServerAndClient((server, client) ->
                    Try.toOptional(server::leaveRoom, client));
            System.out.println("Congratulations! You solved the Sudoku!");
            this.ui.win("Congratulations! You solved the Sudoku!");
        }
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
        this.callServerAndClient((server, client) ->
                playerName.ifPresentOrElse(
                        name -> room.ifPresentOrElse(
                                roomId -> this.joinRoom(server, client, roomId, name),
                                () -> this.createRoom(server, client, name, schema, difficulty)),
                        () -> this.ui.showError(PLAYER_NAME_IS_REQUIRED)
                ));
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onHome() {
        this.callServerAndClient((server, client) -> {
            Try.toOptional(server::leaveRoom, client);
            Try.toOptional(FactoryRMI::shutdownClient, client);
        });
    }

    @Override
    public void onUndo() {
        this.grid.peekUndo().ifPresent(coordinate ->
                this.callServerAndClient((server, client) ->
                        Try.toOptional(server::updateCell, client, coordinate, this.grid.emptyValue())));
    }

    @Override
    public void onSuggest() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        this.callServerAndClient((server, client) ->
                Try.toOptional(server::updateCell, client, coordinate, value));
        return false;
    }


}
