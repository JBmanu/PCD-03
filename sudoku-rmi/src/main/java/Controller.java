import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import rmi.*;
import ui.components.SNumberCell;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;
import utils.Consumers;
import utils.Pair;
import utils.Try;

import java.awt.*;
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
        this.client = Try.toOptional(FactoryRMI::createClient, this, this, this, this, this, this, this);
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
            this.client = Try.toOptional(FactoryRMI::createClient, this, this, this, this, this, this, this);
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
        this.client.flatMap(c -> Try.toOptional(c::roomId)).ifPresent(id -> {
            final Optional<ClientDatas> clientDatas = this.client.flatMap(c -> Try.toOptional(c::datas));
            clientDatas.ifPresent(datas -> this.ui.buildRoom(id + "", datas.name(), this.grid.settings()));
        });
        this.ui.buildGrid(this.grid);
        this.ui.showGridPage();
    }

    @Override
    public void callbackOnJoinRoom(final List<ClientDatas> otherPlayers) {
        final List<Pair<String, Color>> playersColors = otherPlayers.stream()
                .map(player -> Pair.of(player.name(), player.color())).toList();
        this.ui.appendPlayers(playersColors);
    }

    @Override
    public void callbackOnJoinNewPlayer(final ClientDatas newPlayer) {
        this.ui.joinPlayer(newPlayer.name(), newPlayer.color());
    }

    @Override
    public void callbackOnFocusGained(final ClientDatas player, final Coordinate coordinate) {
        this.ui.focusGainedCell(coordinate, player.color());
    }

    @Override
    public void callbackOnFocusLost(final ClientDatas player, final Coordinate coordinate) {
        this.ui.focusLostCell(coordinate);
    }

    @Override
    public void callbackOnMove(final Coordinate coordinate, final int value) {
        if (value == this.grid.emptyValue()) this.grid.undo();
        else this.grid.saveValue(coordinate, value);
        this.ui.writeValueWithoutCheck(coordinate, value);
        this.checkWin();
    }

    @Override
    public void callbackOnLeavePlayer(final ClientDatas player) {
        this.ui.leavePlayer(player.name());
    }

    private void createRoom(final SudokuServer server, final SudokuClient client, final String playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {
        Try.toOptional(client::setName, playerName);
        Try.toOptional(server::createRoom, client, FactoryGrid.settings(schema, difficulty));
        Try.toOptional(FactoryRMI::registerClient, client);
    }

    private void joinRoom(final SudokuServer server, final SudokuClient client, final String roomId, final String playerName) {
        Try.toOptional(client::setName, playerName);
        Try.toOptional(client::setRoomId, Integer.parseInt(roomId));
        Try.toOptional(FactoryRMI::registerClient, client);

        final Optional<SudokuServer.JoinResult> result = Try.toOptional(server::joinRoom, client);
        result.ifPresent(joinResult -> {
            switch (joinResult) {
                case SUCCESS -> {}
                case ROOM_NOT_FOUND -> this.ui.showError("Room " + roomId + " not found");
                case NAME_ALREADY_TAKEN -> this.ui.showError("Name '" + playerName + "' already taken, choose another");
            }
        });
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
        // next feature
    }

    @Override
    public void onReset() {
        // next feature
    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        this.callServerAndClient((server, client) ->
                Try.toOptional(server::updateCell, client, coordinate, value));
        return false;
    }

    @Override
    public void onFocusGainedCell(final SNumberCell cell) {
        this.callServerAndClient((server, client) ->
                Try.toOptional(server::focusGainedCell, client, cell.coordinate()));
    }

    @Override
    public void onFocusLostCell(final SNumberCell cell) {
        this.callServerAndClient((server, client) ->
                Try.toOptional(server::focusLostCell, client, cell.coordinate()));
    }
}
