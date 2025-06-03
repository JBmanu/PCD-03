import model.Coordinate;
import model.Grid;
import model.Settings;
import view.UI;
import view.View;
import view.color.Palette;
import view.components.SNumberCell;
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
//        this.ui.addGridCellInsertListener(this);
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
    public void onChangeCell(final SNumberCell cell) {
        cell.value().ifPresent(value -> this.grid.saveValue(cell.coordinate(), value));
    }

    @Override
    public void onRemoveCell(final SNumberCell cell) {
        cell.value().ifPresentOrElse(
                value -> this.grid.saveValue(cell.coordinate(), value),
                () -> this.grid.resetValue(cell.coordinate()));
    }
}
