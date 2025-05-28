package view.components.documentEvent;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberFilter extends DocumentFilter {

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
            throws BadLocationException {
        if (string != null && string.matches("\\d*")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
            throws BadLocationException {
        if (text != null && text.matches("\\d*")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
