import model.Grid;
import model.Settings;
import view.SudokuUI;
import view.UI;
import view.components.SNumberCell;
import view.listener.*;

import javax.swing.*;

public class Controller implements MenuListener, GridActionListener, NumberInfoListener, GridCellListener, GridCellInsertListener {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Controller controller = new Controller();
        });
    }

    private Grid grid;
    private final UI ui;
    
    public Controller() {
        this.ui = new SudokuUI();

        this.ui.addMenuListener(this);
        this.ui.addGridActionListener(this);
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
    public void onLightMode() {

    }

    @Override
    public void onDarkMode() {

    }

    @Override
    public void onHome() {
        this.ui.showMenuPage();
    }

    @Override
    public void onUndo() {

    }

    @Override
    public void onSuggest() {
        this.grid.suggest();

    }

    @Override
    public void onReset() {

    }

    @Override
    public void onSelected(final int number) {

    }

    @Override
    public void onChangeCell(final SNumberCell SNumberCell) {

    }

    @Override
    public void onRemoveCell(final SNumberCell SNumberCell) {

    }

    @Override
    public void onSelect(final SNumberCell cell) {

    }

    @Override
    public void onHover(final SNumberCell cell) {

    }

    @Override
    public void onExit(final SNumberCell cell) {

    }

    @Override
    public void onFocusGained(final SNumberCell cell) {

    }

    @Override
    public void onFocusLost(final SNumberCell cell) {

    }
}
