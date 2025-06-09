package controller;

import grid.Coordinate;
import grid.Grid;
import grid.Settings;
import model.Player;
import ui.color.Palette;
import utils.Namespace;
import view.GameMultiplayerListener;
import view.UIMultiplayer;
import view.ViewMultiPlayer;

import javax.swing.*;
import java.util.Optional;

public class Controller implements GameMultiplayerListener.PlayerListener {
    private final RabbitMQDiscovery discovery;
    private final RabbitMQConnector connector;
    private final Player player;
    private Grid grid;

    private final UIMultiplayer ui;

    public Controller() {
        this.discovery = RabbitMQDiscovery.create();
        this.connector = RabbitMQConnector.create();
        this.player = Player.create();
        this.ui = new ViewMultiPlayer();

        this.ui.refreshPalette(Palette.light());
        this.ui.addPlayerListener(this);
        this.ui.open();
    }

    @Override
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert name for to play.", "Insert Name", JOptionPane.INFORMATION_MESSAGE);
        }
        if (playerName.isPresent()) {
            this.grid = Grid.create(Settings.create(schema, difficulty));
//            this.connector.createPlayerQueue(playerName.get());
            if (room.isEmpty()) {
                final String countRoom = this.discovery.countExchangesWithoutDefault() + 1 + "";
                final String countQueues = 1 + "";
                this.player.computeToCreateRoom(countRoom, countQueues, playerName.get());
                this.connector.createRoomAndJoin(this.player);
            } else {
                final String roomName = Namespace.computeRoomNameFrom(room.get());
                final String countQueues = this.discovery.countExchangeBinds(roomName) + 1 + "";
                this.player.computeToJoinRoom(room.get(), countQueues, playerName.get());
//                this.connector.createPlayerAndJoin(this.player);
            }
            this.ui.buildGrid(this.grid);
            this.ui.showGridPage();
        }
    }

    @Override
    public void onExit() {
        this.ui.close();

//        final int totalPlayer = this.player.room().map(this.discovery::countExchangeBinds).orElse(0);
//        if (totalPlayer == 1) this.player.room().ifPresent(this.connector::deleteRoom);
//        this.player.queue().ifPresent(this.connector::deletePlayerQueue);
        System.exit(0);
    }

    @Override
    public boolean onModifyCell(final Coordinate coordinate, final int value) {
        return false;
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
}
