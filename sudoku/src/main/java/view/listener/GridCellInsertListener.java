package view.listener;

import view.components.SNumberCell;

public interface GridCellInsertListener {

    void onChangeCell(final SNumberCell SNumberCell);

    void onRemoveCell(final SNumberCell SNumberCell);
}
