package view;

import model.Grid;
import view.listener.GridActionListener;
import view.listener.MenuListener;

public interface UI {

    void open();
    
    void close();

    
    void showMenuPage();

    void showGridPage();


    void buildGrid(Grid grid);
    
    
    void addMenuListener(MenuListener listener);
    
    void addGridActionListener(GridActionListener listener);

}
