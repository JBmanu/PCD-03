import model.Grid;
import model.Settings;
import view.SudokuUI;
import view.UI;
import view.listener.GridActionListener;
import view.listener.MenuListener;

import javax.swing.*;

public class Controller implements MenuListener, GridActionListener {

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

    }

    @Override
    public void onReset() {

    }
}
