package controller;

import grid.Coordinate;
import grid.Grid;
import grid.Settings;
import model.Player;
import utils.Topics;
import view.GameMultiplayerListener;
import view.UIMultiplayer;
import view.ViewMultiPlayer;

import java.util.Optional;

public class Controller implements GameMultiplayerListener.PlayerListener {
    private Optional<RabbitMQDiscovery> discovery;
    private final RabbitMQConnector connector;
    private final Player player;
    private Grid grid;

    private final UIMultiplayer ui;

    public Controller() {
        this.player = Player.create();
        this.ui = new ViewMultiPlayer();

        this.ui.addPlayerListener(this);
        this.ui.open();

        this.loadServices();
        this.connector = RabbitMQConnector.create();
    }

    private void loadServices() {
        do {
            this.discovery = RabbitMQDiscovery.create();
        } while (this.discovery.isEmpty());
        this.ui.showInfo("Connected to RabbitMQ server");
    }

    private void createRoom(final String playerName) {
        this.discovery.ifPresent(discovery -> {
            final String countRoom = discovery.countExchangesWithoutDefault() + 1 + "";
            this.player.computeToCreateRoom(countRoom, "1", playerName);
            this.connector.createRoomAndJoin(this.player);
            this.ui.buildPlayer(this.player);
            this.ui.buildGrid(this.grid);
            this.ui.showGridPage();
        });
    }

    private void joinRoom(final String roomID, final String playerName) {
        this.discovery.ifPresent(discovery -> {
            final String roomName = Topics.computeRoomNameFrom(roomID);
            final String countQueues = discovery.countExchangeBinds(roomName) + 1 + "";
            this.player.computeToJoinRoom(roomID, countQueues, playerName);
            this.ui.buildPlayer(this.player);
            this.ui.appendPlayers(discovery.routingKeysFromBindsExchange(roomName));
            this.connector.joinRoom(discovery, this.player);
            this.connector.sendGridRequest(discovery, this.player);
        });
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
        if (playerName.isEmpty()) {
            this.ui.showError("Please enter a player name.");
        }
        if (playerName.isPresent()) {
            this.grid = Grid.create(Settings.create(schema, difficulty));
            if (room.isEmpty()) {
                this.createRoom(playerName.get());
            } else {
                this.joinRoom(room.get(), playerName.get());
            }

            this.connector.activeCallbackReceiveMessage(this.player, this.grid,
                    this.ui::joinPlayer,
                    this.ui::leavePlayer,
                    (name, coordinate, value) -> {
                        this.grid.saveValue(coordinate, value);
                        this.ui.writeValueWithoutCheck(coordinate, value);
                    }, (solution, cells) -> {
                        this.grid.loadSolution(solution);
                        this.grid.loadCells(cells);
                        this.ui.buildGrid(this.grid);
                        this.ui.showGridPage();
                    });
        }
    }

    @Override
    public void onExit() {
        this.ui.close();
        this.connector.deleteQueue(this.player);
        System.exit(0);
    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        this.grid.saveValue(coordinate, value);
        this.discovery.ifPresent(discovery ->
                this.connector.sendMove(discovery, this.player, coordinate, value));
        return true;
    }

    @Override
    public void onHome() {
        this.discovery.ifPresent(discovery ->
                this.connector.leaveRoom(discovery, this.player));
    }

    @Override
    public void onUndo() {
        this.grid.undo().ifPresent(coordinate -> {
            this.ui.undo(coordinate);
            this.discovery.ifPresent(discovery ->
                    this.connector.sendMove(discovery, this.player, coordinate, this.grid.emptyValue()));
        });
    }

    @Override
    public void onSuggest() {

    }

    @Override
    public void onReset() {

    }
}
