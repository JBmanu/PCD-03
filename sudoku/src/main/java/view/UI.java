package view;

import view.listener.GridActionListener;
import view.listener.MenuListener;

public interface UI {

    void open();
    
    void close();

    
    void showMenuPage();

    void showGridPage();
    

    void addMenuListener(MenuListener listener);
    
    void addGridActionListener(GridActionListener listener);

}
