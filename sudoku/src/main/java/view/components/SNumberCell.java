package view.components;

import model.Coordinate;
import view.color.Palette;
import view.components.documentEvent.DocumentEvent;
import view.components.documentEvent.NumberFilter;
import view.listener.GameListener;
import view.listener.GridPageListener;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static view.utils.StyleUtils.SIZE_CELL_FONT;


public class SNumberCell extends JTextField {
    private static final String SPACE = "";

    private final NumberFilter numberFilter;
    private final DocumentEvent documentEvent;

    private final Coordinate coordinate;
    private final List<GridPageListener.SelectionListener> listeners;

    private Optional<Palette> optionalPalette;

    public SNumberCell(final Coordinate coordinate, final int value) {
        super();

        this.numberFilter = new NumberFilter();
        this.documentEvent = new DocumentEvent(this);

        this.coordinate = coordinate;
        this.listeners = new ArrayList<>();
        this.optionalPalette = Optional.empty();

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
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l ->
                                l.onFocusGainedCell(SNumberCell.this)));
            }

            public void focusLost(final FocusEvent evt) {
                SwingUtilities.invokeLater(() ->
                        SNumberCell.this.listeners.forEach(l ->
                                l.onFocusLostCell(SNumberCell.this)));
            }
        });
    }

    public Coordinate coordinate() {
        return this.coordinate;
    }

    public void setColorable(final Optional<Palette> palette) {
        this.optionalPalette = palette;
        this.colorOnUnselected();
    }

    public void colorOnSelected() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.interaction());
            this.setForeground(color.neutral());
        });
    }

    public void colorOnUnselected() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.neutral());
            this.setForeground(color.secondary());
        });
    }

    public void colorOnHint() {
        this.optionalPalette.ifPresent(color -> {
            this.setBackground(color.feedback());
            this.setForeground(color.secondary());
        });
    }

    public void colorOnHelper() {
        this.optionalPalette.ifPresent(color -> {
            final int alpha = 50;
            this.setBackground(color.interactionWithAlpha(alpha));
            this.setForeground(color.secondary());
        });
    }

    public void addListener(final GridPageListener.SelectionListener listener) {
        this.listeners.add(listener);
    }

    public void addInsertListeners(final GridPageListener.InsertListener listener) {
        this.documentEvent.addListener(listener);
    }

    public void addCellListeners(final GameListener.CellListener listener) {
        this.documentEvent.addCellListener(listener);
    }

    public Optional<Integer> value() {
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
        if (value != 0) this.setEditable(false);
        this.setValue(value);
    }

    public void undo() {
        this.setValue(0);
        this.setEditable(true);
    }
}
