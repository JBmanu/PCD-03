package controller;

import grid.Coordinate;
import grid.Settings;
import model.Player;
import ui.UI;
import ui.color.Palette;
import view.GameMultiplayerListener;
import view.UIMultiplayer;
import view.ViewMultiPlayer;

import java.util.Optional;

public class Controller implements GameMultiplayerListener.PlayerListener {
    private final RabbitMQDiscovery discovery;
    private final RabbitMQConnector connector;
    private final Player player;

    private final UIMultiplayer ui;

    public Controller() {
        this.discovery = RabbitMQDiscovery.create();
        this.connector = RabbitMQConnector.create();
        this.player = Player.create();
        this.ui = new ViewMultiPlayer();

        this.ui.open();
        this.ui.refreshPalette(Palette.light());
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
    public void onStart(final Optional<String> room, final Optional<String> playerName,
                        final Settings.Schema schema, final Settings.Difficulty difficulty) {
    }

    @Override
    public void onExit() {

    }
}
