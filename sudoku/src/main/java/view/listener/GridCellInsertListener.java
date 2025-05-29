package view.listener;

import view.components.SNumberCell;

public interface GridCellInsertListener {

    void onChangeCell(final SNumberCell cell);

    void onRemoveCell(final SNumberCell cell);
}
