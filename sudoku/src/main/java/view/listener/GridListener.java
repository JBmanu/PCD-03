package view.listener;

public interface GridListener {
    
    void onClickCell(final int row, final int column);

    void onHoverCell(final int row, final int column);

    void onFocusCell(final int row, final int column);

    void onChangeCell(final int row, final int column, final int value);
}
