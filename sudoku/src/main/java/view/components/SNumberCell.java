package view.components;

import model.Coordinate;
import utils.IntUtils;
import view.color.Palette;
import view.components.documentEvent.DocumentEvent;
import view.components.documentEvent.NumberFilter;
import view.listener.GameListener;
import view.listener.GridPageListener;
import view.utils.BorderUtils;
import view.utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SNumberCell extends JTextField {
    private static final String EMPTY = "";

    private final NumberFilter numberFilter;
    private final DocumentEvent documentEvent;

    private final Coordinate coordinate;
    private final List<GridPageListener.SelectionListener> listeners;

    private Optional<Palette> optionalPalette;

    public SNumberCell(final Coordinate coordinate, final int value) {
        super();

        this.numberFilter = new NumberFilter(this);
        this.documentEvent = new DocumentEvent(this);

        this.coordinate = coordinate;
        this.listeners = new ArrayList<>();
        this.optionalPalette = Optional.empty();

        this.setSuggest(value);
        this.setFont(StyleUtils.CELL_FONT);
        this.setHorizontalAlignment(JTextField.CENTER);
        this.getDocument().addDocumentListener(this.documentEvent);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(this.numberFilter);

        this.setupListener();
    }

    private void setupListener() {
        this.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onFocusGainedCell(SNumberCell.this)));
            }

            public void focusLost(final FocusEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l -> l.onFocusLostCell(SNumberCell.this)));
            }
        });
    }

    public Coordinate coordinate() {
        return this.coordinate;
    }

    public void setColorable(final Optional<Palette> palette) {
        SwingUtilities.invokeLater(() -> {
            this.optionalPalette = palette;
            this.unselectedColor();
            
            final int alpha = 130;
            palette.ifPresent(color -> BorderUtils.changeColor(this, MatteBorder.class, 
                    color.secondaryWithAlpha(alpha)));
        });
    }

    public void selectionColor() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.interaction());
            this.setForeground(color.neutral());
        });
    }

    public void unselectedColor() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.neutral());
            this.setForeground(this.isEditable() ? color.primary() : color.secondary());
        });
    }

    public void hintColor() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.feedback());
            this.setForeground(color.secondary());
        });
    }

    public void helpColor() {
        this.optionalPalette.ifPresent(color -> {
            final int alpha = 50;
            this.setBackground(color.interactionWithAlpha(alpha));
            this.setForeground(color.secondary());
        });
    }

    public void addSelectionListener(final GridPageListener.SelectionListener listener) {
        this.listeners.add(listener);
    }

    public void addInsertListeners(final GridPageListener.InsertListener listener) {
        this.documentEvent.addListener(listener);
    }

    public void addCellListeners(final GameListener.CellListener listener) {
        this.numberFilter.addListener(listener);
    }

    public Optional<Integer> value() {
        return IntUtils.valueOf(this.getText());
    }

    public void setValue(final int value) {
        this.setText(value == 0 ? EMPTY : String.valueOf(value));
    }

    public void setSuggest(final int value) {
        if (value != 0) this.setEditable(false);
        this.setValue(value);
    }

    public void undo() {
        this.setValue(0);
        this.setEditable(true);
    }
}
