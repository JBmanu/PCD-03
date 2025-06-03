package view.components.documentEvent;

import view.components.SNumberCell;
import view.listener.GridCellInsertListener;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentEvent implements DocumentListener {
    private final List<GridCellInsertListener> listeners;
    private final SNumberCell cell;

    public DocumentEvent(final SNumberCell cell) {
        this.listeners = new ArrayList<>();
        this.cell = cell;
    }

    public void addListener(final GridCellInsertListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final GridCellInsertListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void insertUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            final String insertedText = this.getInsertedText(e);
            if (insertedText.isBlank()) return;
            this.listeners.forEach(listener -> listener.onChangeCell(this.cell));
        });
    }

    @Override
    public void removeUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            this.listeners.forEach(listener -> listener.onRemoveCell(this.cell));
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
