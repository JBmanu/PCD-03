import model.Coordinate;
import model.Grid;
import model.Settings;
import view.UI;
import view.View;
import view.color.Palette;
import view.listener.GameListener;

import java.util.Map;

public class Controller implements GameListener.PlayerListener {
    private Grid grid;
    private final UI ui;

    public Controller() {
        this.ui = new View();

        this.ui.addPlayerListener(this);
        this.ui.refreshPalette(Palette.light());
        this.ui.open();
    }

    @Override
    public void onStart(final Settings.Schema schema, final Settings.Difficulty difficulty) {
        this.grid = Grid.create(Settings.create(schema, difficulty));
        this.ui.buildGrid(this.grid);
        this.ui.showGridPage();
    }

    @Override
    public void onExit() {
        this.ui.close();
        System.exit(0);
    }

    @Override
    public void onUndo() {
        this.grid.undo().ifPresent(this.ui::undo);
    }

    @Override
    public void onSuggest() {
        this.grid.suggest().ifPresent(entry -> this.ui.suggest(entry.getKey(), entry.getValue()));
    }

    @Override
    public void onReset() {
        final Map<Coordinate, Integer> resetGrid = this.grid.reset();
        this.ui.reset(resetGrid);
    }

    @Override
    public void onModifyCell(final Coordinate coordinate, final int value) {
        this.grid.saveValue(coordinate, value);
        if (this.grid.hasWin()) this.ui.win("Congratulations! You solved the Sudoku!");
    }

}
