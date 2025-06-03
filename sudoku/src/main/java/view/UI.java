package view;

import model.Coordinate;
import model.Grid;
import view.components.ColorComponent;
import view.listener.*;

import java.util.Map;

public interface UI extends ColorComponent {

    void open();
    
    void showGridPage();


    void buildGrid(Grid grid);
    
    void setSuggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);


    void addGridActionListener(GridActionListener listener);

    void addNumberInfoListener(NumberInfoListener listener);

    void addGridCellListener(GridCellListener listener);
    
    void addGridCellInsertListener(GridCellInsertListener listener);

}
