package view.listener;

import view.components.SNumberCell;

public interface GridPageListener {

    interface ActionListener extends GameListener.ActionListener {
        void onHome();
    }

    interface SelectionListener {
        void onFocusGainedCell(final SNumberCell cell);

        void onFocusLostCell(final SNumberCell cell);
    }

    interface InsertListener extends GameListener.CellListener {
        
    }
    
    interface CellListener extends SelectionListener, InsertListener {
        
    }
    
}
