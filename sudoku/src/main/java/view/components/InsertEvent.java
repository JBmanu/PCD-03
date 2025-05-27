package view.components;

import view.listener.ChangeCellListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

public class InsertEvent implements DocumentListener {
    private final List<ChangeCellListener> listeners;
    private final JCellView cell;

    public InsertEvent(final JCellView cell) {
        this.listeners = new ArrayList<>();
        this.cell = cell;
    }

    public void addListener(final ChangeCellListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            final String insertedText = this.getInsertedText(e);
            if (insertedText.isBlank() || insertedText.isEmpty()) return;
            this.listeners.forEach(listener -> listener.onChangeCell(this.cell));
        });
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        SwingUtilities.invokeLater(() ->
                this.listeners.forEach(listener -> listener.onRemoveCell(this.cell)));
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
    }

    private String getInsertedText(final DocumentEvent e) {
        try {
            return e.getDocument().getText(e.getOffset(), e.getLength());
        } catch (final Exception ex) {
            return "";
        }
    }
}
