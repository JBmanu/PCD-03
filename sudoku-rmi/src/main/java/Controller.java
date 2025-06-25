import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import rmi.*;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;
import utils.Try;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Controller implements Serializable, GameMultiplayerListener.PlayerListener {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String CONNECTED_TO_SERVER = "Connected to server";
    public static final String SERVER_IS_NOT_AVAILABLE = "Server is not available";
    public static final String PLAYER_NAME_IS_REQUIRED = "Player name is required";
    public static final String INVALID_ROOM_ID = "Invalid room ID";

    private Optional<SudokuServer> server;
    private final Optional<SudokuClient> client;
    private final UIMultiplayer ui;
    private Grid grid;

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
            this.server = FactoryRMI.retrieveServer();
            this.ui.showInfo(CONNECTED_TO_SERVER);
        } while (this.server.isEmpty());
    }


    private void callServer(final Consumer<SudokuServer> consumer) {
        this.server.ifPresentOrElse(
                consumer,
                () -> this.ui.showError(SERVER_IS_NOT_AVAILABLE));
    }

    private void createRoom(final String playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {
//        this.callServer(server -> {
//            final Settings settings = FactoryGrid.settings(schema, difficulty);
//            this.client = Try.toOptional((A client1, B settings1, C callbackMove, D callbackPlayers, E callbackLeavePlayer, F callbackGrid) -> server.createRoom(client1, settings1, callbackGrid),
//                    playerName, settings,
//                    (solution, cells) -> {
//                        // MOVE
//                    },
//                    (players) -> {
//                    }, //this.ui.appendPlayers(List.of(players)),
////                    this.ui::leavePlayer,
//                    _ -> {
//                    },
//                    (solution, cells) -> {
////                        this.grid = FactoryGrid.gridAndLoadData(settings, solution, cells);
////                        this.ui.buildGrid(this.grid);
////                        this.client.map(client -> Try.toOptional(client::roomId))
////                                .ifPresent(id -> this.ui.buildRoom(id + ""));
//
//                        SwingUtilities.invokeLater(() -> {
//                            this.ui.showGridPage();
//                        });
//                    });
//        });
    }

    private void joinRoom(final String roomId, final String playerName) {
//        this.callServer(server -> {
//            final Optional<Integer> roomCode = Try.toOptional(roomId);
//            roomCode.ifPresentOrElse(id ->
//                            this.client = Try.toOptional((String namePlayer, Integer roomId1, CallbackClient.CallbackMove callbackMove, CallbackClient.CallbackJoinPlayers callbackPlayers, CallbackClient.CallbackLeavePlayer callbackLeavePlayer, CallbackServer.CallbackGrid callbackGrid, CallbackServer.CallbackJoinPlayers callbackJoinPlayers) -> server.joinRoom(, namePlayer, callbackMove, callbackPlayers, callbackLeavePlayer, callbackGrid, callbackJoinPlayers), playerName, id,
//                                    (coordinate, value) -> {
//                                        // MOVE
//                                    },
//                                    (players) -> this.ui.appendPlayers(List.of(players)),
//                                    this.ui::leavePlayer,
//                                    (solution, cells) -> {
//                                        this.grid = FactoryGrid.gridLoad(solution, cells);
//                                        this.ui.buildRoom(roomId);
//                                        this.ui.buildGrid(this.grid);
//                                        this.ui.showGridPage();
//                                    }, this.ui::appendPlayers),
//                    () -> this.ui.showError(INVALID_ROOM_ID));
//        });
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
