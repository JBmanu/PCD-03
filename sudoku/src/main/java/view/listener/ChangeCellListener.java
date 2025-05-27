package view.listener;

import view.components.JCellView;

public interface ChangeCellListener {

    void onChangeCell(final JCellView JCellView);

    void onRemoveCell(final JCellView JCellView);
}
