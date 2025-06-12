package ui.panel;

import ui.color.Palette;
import ui.components.ColorComponent;
import ui.components.SButton;
import ui.listener.GameListener;
import ui.listener.GridPageListener;
import ui.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.utils.StyleUtils.*;

public class GridActionPanel extends JPanel implements ColorComponent {
    private final List<GridPageListener.ActionListener> listeners;
    private final List<GameListener.ActionListener> actionListeners;

    private final SButton home;
    private final SButton undo;
    private final SButton suggest;
    private final SButton reset;

    public GridActionPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, ZERO_GAP));
        PanelUtils.transparent(this);

        this.listeners = new ArrayList<>();
        this.actionListeners = new ArrayList<>();

        this.home = new SButton("Home");
        this.undo = new SButton("Undo");
        this.suggest = new SButton("Suggest");
        this.reset = new SButton("reset");

        final List<SButton> buttons = List.of(this.home, this.undo, this.suggest, this.reset);
        buttons.forEach(button -> button.setFont(FONT_GAME));
        buttons.forEach(this::add);

        this.home.addActionListener(_ -> this.onClickHome());
        this.undo.addActionListener(_ -> this.onClickUndo());
        this.suggest.addActionListener(_ -> this.onClickSuggest());
        this.reset.addActionListener(_ -> this.onClickReset());
    }

    private void onClickHome() {
        this.listeners.forEach(GridPageListener.ActionListener::onHome);
        this.actionListeners.forEach(GameListener.ActionListener::onHome);
    }

    private void onClickUndo() {
        this.listeners.forEach(GridPageListener.ActionListener::onUndo);
        this.actionListeners.forEach(GameListener.ActionListener::onUndo);
    }

    private void onClickSuggest() {
        this.listeners.forEach(GridPageListener.ActionListener::onSuggest);
        this.actionListeners.forEach(GameListener.ActionListener::onSuggest);
    }

    private void onClickReset() {
        this.listeners.forEach(GridPageListener.ActionListener::onReset);
        this.actionListeners.forEach(GameListener.ActionListener::onReset);
    }

    public void addListener(final GridPageListener.ActionListener listener) {
        this.listeners.add(listener);
    }

    public void addActionListener(final GameListener.ActionListener listener) {
        this.actionListeners.add(listener);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.home.refreshPalette(palette);
        this.undo.refreshPalette(palette);
        this.suggest.refreshPalette(palette);
        this.reset.refreshPalette(palette);
    }
}
