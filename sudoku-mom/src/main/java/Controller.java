import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import model.Player;
import rabbitMQ.RabbitMQConnector;
import rabbitMQ.RabbitMQDiscovery;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;
import utils.MOMConsumers;
import utils.Topics;

import java.util.Optional;

public class Controller implements GameMultiplayerListener.PlayerListener {
    public static final String ERROR_DISCOVERY = "Discovery not available.";
    public static final String ERROR_CONNECTOR = "Connector not available.";
    public static final String INFO_PLAYER = "Enter a player name.";
    private Optional<RabbitMQDiscovery> discovery;
    private Optional<RabbitMQConnector> connector;
    private final Player player;
    private Grid grid;

    private final UIMultiplayer ui;

    public Controller() {
        this.player = Player.create();
        this.ui = new ViewMultiPlayer();

        this.ui.addPlayerListener(this);
        this.ui.open();

        this.discovery = Optional.empty();
        this.connector = Optional.empty();

        this.loadServices();
    }

    private void loadServices() {
        do this.discovery = RabbitMQDiscovery.create(); while (this.discovery.isEmpty());
        do this.connector = RabbitMQConnector.create(); while (this.connector.isEmpty());
        this.ui.showInfo("Connected to RabbitMQ server");
    }

    private void callRabbitMQ(final MOMConsumers.CallRabbitMQ callRabbitMQ) {
        this.discovery.ifPresentOrElse(discovery ->
                        this.connector.ifPresentOrElse(connector ->
                                        callRabbitMQ.accept(discovery, connector),
                                () -> this.ui.showError(ERROR_CONNECTOR)),
                () -> this.ui.showError(ERROR_DISCOVERY));
    }

    private void createRoom(final String playerName, final Settings.Schema schema, final Settings.Difficulty difficulty) {
        this.callRabbitMQ((discovery, connector) -> {
            this.grid = FactoryGrid.grid(FactoryGrid.settings(schema, difficulty));
            final String countRoom = discovery.countExchangesWithoutDefault() + 1 + "";
            this.player.computeToCreateRoom(countRoom, "1", playerName);
            connector.createRoomAndJoin(this.player);
            this.player.computeRoomID().ifPresent(this.ui::buildRoom);
            this.ui.buildGrid(this.grid);
            this.ui.showGridPage();
        });
    }

    private void joinRoom(final String roomID, final String playerName) {
        this.callRabbitMQ((discovery, connector) -> {
            final String roomName = Topics.computeRoomNameFrom(roomID);
            final String countQueues = discovery.countExchangeBinds(roomName) + 1 + "";
            this.player.computeToJoinRoom(roomID, countQueues, playerName);
            this.player.computeRoomID().ifPresent(this.ui::buildRoom);
            this.ui.appendPlayers(discovery.routingKeysFromBindsExchange(roomName));
            connector.joinRoom(discovery, this.player);
            connector.sendGridRequest(discovery, this.player);
        });
    }

    private void checkWin() {
        if (this.grid.hasWin()) {
            this.ui.win("Congratulations! You solved the Sudoku!");
            this.callRabbitMQ((discovery, connector) -> connector.leaveRoom(discovery, this.player));
        }
    }

    private String infoMove(final String name, final Coordinate coordinate, final int value) {
        return name + ": [" + coordinate.row() + ", " + coordinate.col() + "] => " + value;
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
        this.callRabbitMQ((_, connector) ->
                playerName.ifPresentOrElse(
                        myName -> {
                            room.ifPresentOrElse(
                                    roomID -> this.joinRoom(roomID, myName),
                                    () -> this.createRoom(myName, schema, difficulty));
                            connector.activeCallbackReceiveMessage(this.player, this.grid,
                                    this.ui::joinPlayer,
                                    this.ui::leavePlayer,
                                    (name, coordinate, value) -> {
                                        if (value == this.grid.emptyValue()) this.grid.undo();
                                        else this.grid.saveValue(coordinate, value);
                                        this.ui.writeValueWithoutCheck(coordinate, value);
                                        this.ui.showInfo(this.infoMove(name, coordinate, value));
                                        this.checkWin();
                                    }, (newSchema, newDifficulty, solution, cells) -> {
                                        this.grid = FactoryGrid.grid(FactoryGrid.settings(newSchema, newDifficulty));
                                        this.grid.loadSolution(solution);
                                        this.grid.loadCells(cells);
                                        this.ui.buildGrid(this.grid);
                                        this.ui.showGridPage();
                                    });
                        },
                        () -> this.ui.showError(INFO_PLAYER)
                ));
    }

    @Override
    public void onExit() {
        this.ui.close();
        this.callRabbitMQ((discovery, connector) -> connector.deleteQueue(discovery, this.player));
        System.exit(0);
    }

    @Override
    public void onHome() {
        this.callRabbitMQ((discovery, connector) -> connector.leaveRoom(discovery, this.player));
    }

    @Override
    public void onUndo() {
        this.grid.peekUndo().ifPresent(coordinate -> {
            this.callRabbitMQ((discovery, connector) ->
                    connector.sendMove(discovery, this.player, coordinate, this.grid.emptyValue()));
        });
    }

    @Override
    public void onSuggest() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        this.callRabbitMQ((discovery, connector) ->
                connector.sendMove(discovery, this.player, coordinate, value));
        return false;
    }
}
