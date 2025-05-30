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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        this.setOpaque(false);
        this.colorable = Colorable.test();

        this.setSuggest(value);
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
                    SNumberCell.this.listeners.forEach(l -> l.onFocusGainedCell(SNumberCell.this));
//                    SNumberCell.this.setForeground(SNumberCell.this.fgColorHover);
//                    SNumberCell.this.setCaretColor(SNumberCell.this.cursorColorHover);
                });
            }

            public void focusLost(final FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    SNumberCell.this.listeners.forEach(l -> l.onFocusLostCell(SNumberCell.this));
//                    SNumberCell.this.setForeground(SNumberCell.this.fgColorDefault);
//                    SNumberCell.this.setCaretColor(SNumberCell.this.cursorColorDefault);
                });
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onSelectCell(SNumberCell.this)));
            }

            @Override
            public void mouseEntered(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onHoverCell(SNumberCell.this)));
            }

            @Override
            public void mouseExited(final MouseEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onExitCell(SNumberCell.this)));
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

    public Optional<Integer> getValue() {
        try {
            return Optional.of(Integer.parseInt(this.getText().trim()));
        } catch (final NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void setValue(final int value) {
        this.setText(value == 0 ? SPACE : String.valueOf(value));
    }
    
    public void setSuggest(final int value) {
        if (value != 0) this.setEnabled(false);
        this.setValue(value);
    }

    public void undo() {
        this.setValue(0);
        this.setEnabled(true);
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
