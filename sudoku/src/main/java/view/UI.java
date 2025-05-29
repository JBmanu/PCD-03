package view;

import model.Grid;
import view.listener.*;

public interface UI {

    void open();
    
    void close();

    
    void showMenuPage();

    void showGridPage();


    void buildGrid(Grid grid);
    
    
    void addMenuListener(MenuListener listener);
    
    void addGridActionListener(GridActionListener listener);

    void addNumberInfoListener(NumberInfoListener listener);

    void addGridCellListener(GridCellListener listener);
    
    void addGridCellInsertListener(GridCellInsertListener listener);
    
}
