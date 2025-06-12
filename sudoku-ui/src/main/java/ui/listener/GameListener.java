package ui.listener;

import grid.Coordinate;
import grid.Settings;

public interface GameListener {

    interface StartListener {
        void onStart(final Settings.Schema schema, final Settings.Difficulty difficulty);

        void onExit();
    }

    interface ActionListener {
        void onHome();
        
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
