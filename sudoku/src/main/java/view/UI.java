package view;

import model.Coordinate;
import model.Grid;
import view.components.ColorComponent;
import view.listener.GameListener;

import java.util.Map;

public interface UI extends ColorComponent {

    void open();
    
    void close();
    
    void showGridPage();

    
    void addPlayerListener(GameListener.PlayerListener listener);
    

    void buildGrid(Grid grid);
    
    void suggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);


    void win();
}
