package view;

import grid.Coordinate;
import grid.Grid;
import ui.components.ColorComponent;
import ui.listener.GameListener;

import java.util.Map;

public interface UIMultiplayer extends ColorComponent {

    void open();
    
    void close();
    
    void showGridPage();

    
    void addPlayerListener(GameMultiplayerListener.PlayerListener listener);
    

    void buildGrid(Grid grid);
    
    void suggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);
    
    void writeValue(Coordinate coordinate, Integer value);


    void win(final String message);
}
