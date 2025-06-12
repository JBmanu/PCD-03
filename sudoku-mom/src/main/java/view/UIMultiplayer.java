package view;

import grid.Coordinate;
import grid.Grid;
import model.Player;
import ui.components.ColorComponent;

import java.util.List;
import java.util.Map;

public interface UIMultiplayer extends ColorComponent {

    void open();
    
    void close();
    
    void showGridPage();

    
    void addPlayerListener(GameMultiplayerListener.PlayerListener listener);

    
    void buildGrid(Grid grid);
    
    void buildPlayer(Player player);

    void joinPlayer(String newPlayerName);

    void appendPlayers(List<String> currentPlayers);
    
    void suggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);
    
    void writeValueWithoutCheck(Coordinate coordinate, Integer value);


    void win(final String message);

}
