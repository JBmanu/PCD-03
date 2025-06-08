package view;

import grid.Coordinate;
import grid.Settings;

import java.util.Optional;

public interface GameMultiplayerListener {

    interface StartListener {
        void onStart(final Optional<String> room, final Optional<String> playerName,
                     final Settings.Schema schema, final Settings.Difficulty difficulty);

        void onExit();
    }

    interface ActionListener {
        void onUndo();

        void onSuggest();

        void onReset();
    }

    interface CellListener {
        boolean onModifyCell(final Coordinate coordinate, final int value);
    }

    interface PlayerListener extends StartListener, ActionListener, CellListener {

    }
}
