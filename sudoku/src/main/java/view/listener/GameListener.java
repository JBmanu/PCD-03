package view.listener;

import model.Settings;
import view.components.SNumberCell;

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
        void onChangeCell(final SNumberCell cell);

        void onRemoveCell(final SNumberCell cell);
    }

    interface PlayerListener extends StartListener, ActionListener, CellListener {

    }
}
