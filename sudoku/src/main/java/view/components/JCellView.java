package view.components;

import model.Coordinate;
import view.color.Palette;
import view.listener.ChangeCellListener;
import view.listener.FocusCellListener;
import view.listener.OnHoverCellListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.SIZE_CELL;
import static view.utils.StyleUtils.SIZE_CELL_FONT;


public class JCellView extends JTextField implements ColorComponent {
    private static final String SPACE = "";
    private final Coordinate position;
    private final InsertEvent insertEvent;
    private final List<FocusCellListener> focusListeners;
    private final List<OnHoverCellListener> hoverListeners;

    private Color cursorColorDefault;
    private Color cursorColorHover;
    private Color fgColorDefault;
    private Color fgColorHover;

    public JCellView(final Coordinate position, final int value) {
        super();
        this.position = position;
        this.insertEvent = new InsertEvent(this);
        this.focusListeners = new ArrayList<>();
        this.hoverListeners = new ArrayList<>();
        if (value != 0) {
            this.setEnabled(false);
        }
        this.setValue(value);
        this.setPreferredSize(new Dimension(SIZE_CELL, SIZE_CELL));
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setFont(this.getFont().deriveFont(SIZE_CELL_FONT));
        this.getDocument().addDocumentListener(this.insertEvent);
        
        this.setupListener();
    }

    private void setupListener() {
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(final java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    JCellView.this.focusListeners.forEach(listener -> listener.onFocusCell(JCellView.this));
                    JCellView.this.setForeground(JCellView.this.fgColorHover);
                    JCellView.this.setCaretColor(JCellView.this.cursorColorHover);
                });
            }

            public void focusLost(final java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    JCellView.this.focusListeners.forEach(listener -> listener.onUnFocusCell(JCellView.this));
                    JCellView.this.setForeground(JCellView.this.fgColorDefault);
                    JCellView.this.setCaretColor(JCellView.this.cursorColorDefault);
                });
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(final java.awt.event.MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        JCellView.this.hoverListeners.forEach(listener -> listener.onHoverCell(JCellView.this)));
            }

            public void mouseExited(final java.awt.event.MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        JCellView.this.hoverListeners.forEach(listener -> listener.onUnHoverCell(JCellView.this)));
            }
        });

    }

    public void addListener(final ChangeCellListener listener) {
        this.insertEvent.addListener(listener);
    }

    public void addListener(final FocusCellListener listener) {
        this.focusListeners.add(listener);
    }

    public void addListener(final OnHoverCellListener listener) {
        this.hoverListeners.add(listener);
    }

    public int getValue() {
        try {
            return Integer.parseInt(this.getText());
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    public void setValue(final int value) {
        this.setText(value == 0 ? SPACE : String.valueOf(value));
    }

    public Coordinate position() {
        return this.position;
    }

    @Override
    public void refreshPalette(final Palette palette) {
//        if (!this.isEnabled()) this.setForeground(COLOR_SCHEME.disabled());
//        this.cursorColorDefault = COLOR_SCHEME.third();
//        this.cursorColorHover = COLOR_SCHEME.second();
//
//        this.fgColorDefault = COLOR_SCHEME.second();
//        this.fgColorHover = COLOR_SCHEME.third();
//
//        this.setBackground(COLOR_SCHEME.background());
//        this.setForeground(COLOR_SCHEME.second());  
    }
}
