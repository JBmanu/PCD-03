package view.listener;

import model.Coordinate;
import model.Settings;

public interface GameListener {

    interface StartListener {
        void onStart(final Settings.Schema schema, final Settings.Difficulty difficulty);

        void onExit();
    }

    interface ActionListener {
        void onUndo();

        void onSuggest();

        void onReset();
    }

    interface CellListener {
        void onModifyCell(final Coordinate coordinate, final int value);
    }

    interface PlayerListener extends StartListener, ActionListener, CellListener {

    }
}
