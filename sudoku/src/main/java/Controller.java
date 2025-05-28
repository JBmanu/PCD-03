import model.Grid;
import view.SudokuUI;
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
    private final SudokuUI ui;
    
    public Controller() {
        this.ui = new SudokuUI();
        this.ui.addGridActionListener(this);
        
        this.ui.setVisible(true);
    }

    @Override
    public void onStart(final String schema, final String difficulty) {
        
    }

    @Override
    public void onExit() {

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
