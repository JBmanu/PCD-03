package ui.components;

import grid.Coordinate;
import utils.IntUtils;
import ui.color.Palette;
import ui.components.documentEvent.DocumentEvent;
import ui.components.documentEvent.NumberFilter;
import ui.listener.GameListener;
import ui.listener.GridPageListener;
import ui.utils.BorderUtils;
import ui.utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SNumberCell extends JTextField implements ColorComponent {
    private static final String EMPTY = "";

    private final NumberFilter numberFilter;
    private final DocumentEvent documentEvent;

    private final Coordinate coordinate;
    private final List<GridPageListener.SelectionListener> listeners;

    private Optional<Palette> optionalPalette;

    public SNumberCell(final Coordinate coordinate, final int value) {
        super();
        this.setOpaque(false);

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

//    public void setColorable(final Optional<Palette> palette) {
//        SwingUtilities.invokeLater(() -> {
//            this.optionalPalette = palette;
//            this.unselectedColor();
//            
//            final int alpha = 130;
//            palette.ifPresent(color -> BorderUtils.changeColor(this, MatteBorder.class, 
//                    color.secondaryWithAlpha(alpha)));
//        });
//    }

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
    
    private void removeAllDocumentListeners(){
        ((AbstractDocument) this.getDocument()).setDocumentFilter(null);
        this.getDocument().removeDocumentListener(this.documentEvent);
    }
    
    private void restoAllDocumentListeners() {
        this.getDocument().addDocumentListener(this.documentEvent);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(this.numberFilter);
    }
    
    public void setValueWithoutCheck(final int value) {
        this.removeAllDocumentListeners();
        this.setValue(value);
        this.restoAllDocumentListeners();
    }

    public void setSuggest(final int value) {
        if (value != 0) this.setEditable(false);
        this.setValue(value);
    }

    public void undo() {
        this.setValue(0);
        this.setEditable(true);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        final Color bg = this.getBackground();
        g2.setColor(bg);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        SwingUtilities.invokeLater(() -> {
            this.optionalPalette = Optional.of(palette);
            this.unselectedColor();

            final int alpha = 130;
            BorderUtils.changeColor(this, MatteBorder.class, palette.secondaryWithAlpha(alpha));
        });
//        this.setColorable(Optional.of(palette));
    }
}
