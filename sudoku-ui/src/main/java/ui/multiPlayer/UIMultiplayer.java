package ui.multiPlayer;

import grid.Coordinate;
import grid.Grid;
import ui.components.ColorComponent;
import ui.multiPlayer.panel.InfoPanel;

import java.util.List;
import java.util.Map;

public interface UIMultiplayer extends ColorComponent, InfoPanel {

    void open();
    
    void close();
    
    void showGridPage();

    
    void addPlayerListener(GameMultiplayerListener.PlayerListener listener);

    
    void buildGrid(Grid grid);
    
    void buildRoom(String roomId);

    void joinPlayer(String newPlayerName);

    void leavePlayer(String playerName);

    void appendPlayers(List<String> currentPlayers);
    
    void suggest(Coordinate key, Integer value);

    void undo(Coordinate coordinate);
    
    void reset(Map<Coordinate, Integer> resetGrid);
    
    void writeValueWithoutCheck(Coordinate coordinate, Integer value);


    void win(final String message);

}
