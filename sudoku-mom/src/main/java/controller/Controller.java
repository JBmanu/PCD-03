package controller;

import grid.Coordinate;
import grid.Settings;
import model.Player;
import ui.UI;
import ui.color.Palette;
import ui.listener.GameListener;
import view.ViewMultiPlayer;

public class Controller implements GameListener.PlayerListener {
    private final RabbitMQDiscovery discovery;
    private final RabbitMQConnector connector;
    private final Player player;
    
    private final UI ui;
    
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
    public void onStart(final Settings.Schema schema, final Settings.Difficulty difficulty) {

    }

    @Override
    public void onExit() {

    }
}
