package view;

import grid.Settings;
import ui.listener.GameListener;

import java.util.Optional;

public interface GameMultiplayerListener {

    interface StartListener {
        void onStart(final Optional<String> room, final Optional<String> playerName,
                     final Settings.Schema schema, final Settings.Difficulty difficulty);

        void onExit();
    }
    
    interface PlayerListener extends StartListener, GameListener.ActionListener, GameListener.CellListener {

    }
}
