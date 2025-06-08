package ui.components.documentEvent;

import ui.components.SNumberCell;
import ui.listener.GridPageListener;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentEvent implements DocumentListener {
    private final List<GridPageListener.InsertListener> listeners;

    private final SNumberCell cell;

    public DocumentEvent(final SNumberCell cell) {
        this.listeners = new ArrayList<>();
        this.cell = cell;
    }

    public void addListener(final GridPageListener.InsertListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void insertUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> 
                this.listeners.forEach(listener -> listener.onModifyCell(this.cell)));
    }

    @Override
    public void removeUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> 
                this.listeners.forEach(listener -> listener.onModifyCell(this.cell)));
    }

    @Override
    public void changedUpdate(final javax.swing.event.DocumentEvent e) {
        SwingUtilities.invokeLater(() -> 
                this.listeners.forEach(listener -> listener.onModifyCell(this.cell)));
    }

}
