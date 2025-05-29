package view.listener;

import view.components.SNumberCell;

public interface GridCellListener {

    void onSelectCell(final SNumberCell cell);

    void onHoverCell(final SNumberCell cell);

    void onExitCell(final SNumberCell cell);

    void onFocusGainedCell(final SNumberCell cell);

    void onFocusLostCell(final SNumberCell cell);

}