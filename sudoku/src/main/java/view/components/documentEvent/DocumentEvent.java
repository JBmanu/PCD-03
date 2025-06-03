package view.components.documentEvent;

import view.components.SNumberCell;
import view.listener.GameListener;
import view.listener.GridPageListener;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentEvent implements DocumentListener {
    private final List<GridPageListener.InsertListener> listeners;
    private final List<GameListener.CellListener> cellListeners;

    private final SNumberCell cell;

    public DocumentEvent(final SNumberCell cell) {
        this.listeners = new ArrayList<>();
        this.cellListeners = new ArrayList<>();
        this.cell = cell;
    }

    public void addListener(final GridPageListener.InsertListener listener) {
        this.listeners.add(listener);
    }

    public void addCellListener(final GameListener.CellListener listener) {
        this.cellListeners.add(listener);
    }

    @Override
    public void insertUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            final String insertedText = this.getInsertedText(e);
            if (insertedText.isBlank()) return;
            this.listeners.forEach(listener -> listener.onChangeCell(this.cell));
            this.cellListeners.forEach(listener -> listener.onChangeCell(this.cell));
        });
    }

    @Override
    public void removeUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            this.listeners.forEach(listener -> listener.onRemoveCell(this.cell));
            this.cellListeners.forEach(listener -> listener.onRemoveCell(this.cell));
        });
    }

    @Override
    public void changedUpdate(final javax.swing.event.DocumentEvent e) {
    }

    private String getInsertedText(final javax.swing.event.DocumentEvent e) {
        try {
            return e.getDocument().getText(e.getOffset(), e.getLength());
        } catch (final Exception ex) {
            return "";
        }
    }
}
