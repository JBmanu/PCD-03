package view.listener;

import view.components.SNumberCell;

public interface GridCellListener {
    
    void onFocusGainedCell(final SNumberCell cell);

    void onFocusLostCell(final SNumberCell cell);
}