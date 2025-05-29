package view;

import model.Coordinate;
import model.Grid;
import view.listener.*;

public interface UI {

    void open();
    
    void close();

    
    void showMenuPage();

    void showGridPage();


    void buildGrid(Grid grid);
    
    void setSuggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);


    void addMenuListener(MenuListener listener);
    
    void addGridActionListener(GridActionListener listener);

    void addNumberInfoListener(NumberInfoListener listener);

    void addGridCellListener(GridCellListener listener);
    
    void addGridCellInsertListener(GridCellInsertListener listener);

}
