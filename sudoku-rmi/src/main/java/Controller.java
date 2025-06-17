import grid.Coordinate;
import grid.Settings;
import rmi.SudokuClient;
import rmi.SudokuServer;
import ui.multiPlayer.GameMultiplayerListener;
import ui.multiPlayer.UIMultiplayer;
import ui.multiPlayer.ViewMultiPlayer;

import java.util.Optional;

public class Controller implements GameMultiplayerListener.PlayerListener {
    private final Optional<SudokuServer> server;
    private final Optional<SudokuClient> client;
    private final UIMultiplayer ui;
    
    public Controller() {
        this.ui = new ViewMultiPlayer();
        this.client = Optional.empty();
        this.server = Optional.empty();

        this.ui.addPlayerListener(this);
        this.ui.open();
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
