package ui.listener;

import ui.components.SNumberCell;

public interface GridPageListener {

    interface ActionListener extends GameListener.ActionListener {
        void onHome();
        
//        void onDarkMode();
//        
//        void onLightMode();
    }

    interface SelectionListener {
        void onFocusGainedCell(final SNumberCell cell);

        void onFocusLostCell(final SNumberCell cell);
    }

    interface InsertListener {
        void onModifyCell(final SNumberCell cell);
    }
    
    interface CellListener extends SelectionListener, InsertListener {
        
    }
    
}
