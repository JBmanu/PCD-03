package view;

import model.Coordinate;
import model.Grid;
import view.listener.*;

import java.util.Map;

public interface UI {

    void open();
    
    void close();

    
    void showMenuPage();

    void showGridPage();


    void buildGrid(Grid grid);
    
    void setSuggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);


    void addMenuListener(MenuListener listener);
    
    void addGridActionListener(GridActionListener listener);

    void addNumberInfoListener(NumberInfoListener listener);

    void addGridCellListener(GridCellListener listener);
    
    void addGridCellInsertListener(GridCellInsertListener listener);

}
