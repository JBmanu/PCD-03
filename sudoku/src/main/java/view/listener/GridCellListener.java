package view.listener;

import view.components.SNumberCell;

public interface GridCellListener {

    void onSelect(final SNumberCell cell);

    void onHover(final SNumberCell cell);

    void onExit(final SNumberCell cell);

    void onFocusGained(final SNumberCell cell);

    void onFocusLost(final SNumberCell cell);

}