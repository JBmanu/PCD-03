package view.components;

import model.Coordinate;
import view.color.Colorable;
import view.color.Palette;
import view.components.documentEvent.DocumentEvent;
import view.components.documentEvent.NumberFilter;
import view.listener.GridCellInsertListener;
import view.listener.GridCellListener;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.SIZE_CELL_FONT;


public class SNumberCell extends JTextField implements ColorComponent {
    private static final String SPACE = "";

    private final NumberFilter numberFilter;
    private final DocumentEvent documentEvent;
    
    
    private final Coordinate coordinate;
    private final List<GridCellListener> listeners;

    private final Colorable colorable;

    public SNumberCell(final Coordinate coordinate, final int value) {
        super();

        this.numberFilter = new NumberFilter();
        this.documentEvent = new DocumentEvent(this);
        
        this.coordinate = coordinate;
        this.listeners = new ArrayList<>();
        this.colorable = Colorable.test();

        if (value != 0) this.setEnabled(false);
        this.setValue(value);
        
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setFont(this.getFont().deriveFont(SIZE_CELL_FONT));
        this.getDocument().addDocumentListener(this.documentEvent);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(this.numberFilter);

        this.setupListener();
    }

    private void setupListener() {
        this.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    SNumberCell.this.listeners.forEach(l -> l.onFocusGained(SNumberCell.this));
//                    SNumberCell.this.setForeground(SNumberCell.this.fgColorHover);
//                    SNumberCell.this.setCaretColor(SNumberCell.this.cursorColorHover);
                });
            }

            public void focusLost(final FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    SNumberCell.this.listeners.forEach(l -> l.onFocusLost(SNumberCell.this));
//                    SNumberCell.this.setForeground(SNumberCell.this.fgColorDefault);
//                    SNumberCell.this.setCaretColor(SNumberCell.this.cursorColorDefault);
                });
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onSelect(SNumberCell.this)));
            }

            @Override
            public void mouseEntered(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onHover(SNumberCell.this)));
            }

            @Override
            public void mouseExited(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onExit(SNumberCell.this)));
            }
        });

    }

    public Coordinate coordinate() {
        return this.coordinate;
    }
    
    public void addListener(final GridCellListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final GridCellListener listener) {
        this.listeners.remove(listener);
    }
    
    public void addInsertListeners(final GridCellInsertListener listener) {
        this.documentEvent.addListener(listener);
    }
    
    public void removeInsertListeners(final GridCellInsertListener listener) {
        this.documentEvent.removeListener(listener);
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
