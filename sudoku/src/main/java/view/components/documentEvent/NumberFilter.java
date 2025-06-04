package view.components.documentEvent;

import view.components.SNumberCell;
import view.listener.GameListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.ArrayList;
import java.util.List;

public class NumberFilter extends DocumentFilter {
    private final List<GameListener.CellListener> cellListeners;
    private final SNumberCell cell;

    public NumberFilter(final SNumberCell cell) {
        this.cellListeners = new ArrayList<>();
        this.cell = cell;
    }

    public void addListener(final GameListener.CellListener listener) {
        this.cellListeners.add(listener);
    }

    private boolean isValidNumber(final String text) {
        return text != null && text.matches("\\d*");
    }

    private void notifyListeners(final String text) {
        final int value = text.isEmpty() ? 0 : Integer.parseInt(text);
        this.cellListeners.forEach(listener -> listener.onModifyCell(this.cell.coordinate(), value));
    }

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr) throws BadLocationException {
        final StringBuilder newText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        newText.insert(offset, string);

        if (this.isValidNumber(newText.toString())) {
            this.notifyListeners(newText.toString());
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
        final StringBuilder newText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        newText.replace(offset, offset + length, text);

        if (this.isValidNumber(newText.toString())) {
            this.notifyListeners(newText.toString());
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
        final StringBuilder newText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        newText.delete(offset, offset + length);

        if (this.isValidNumber(newText.toString())) {
            this.notifyListeners(newText.toString());
            super.remove(fb, offset, length);
        }
    }
}
