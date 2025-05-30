package view.panel;

import view.color.Colorable;
import view.color.Palette;
import view.components.ColorComponent;
import view.components.SButton;
import view.listener.GridActionListener;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.*;

public class GridActionPanel extends JPanel implements ColorComponent {
    private final List<GridActionListener> listeners;
    private final SButton home;
    private final SButton undo;
    private final SButton suggest;
    private final SButton reset;
    
    public GridActionPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, ZERO_GAP));
        PanelUtils.transparent(this);

        this.listeners = new ArrayList<>();
        this.home = new SButton("Home");
        this.undo = new SButton("Undo");
        this.suggest = new SButton("Suggest");
        this.reset = new SButton("reset");

        final List<SButton> buttons = List.of(this.home, this.undo, this.suggest, this.reset);
        buttons.forEach(button -> button.setFont(FONT_GAME));
        buttons.forEach(this::add);

        this.home.addActionListener(e -> this.onClickHome());
        this.undo.addActionListener(e -> this.onClickUndo());
        this.suggest.addActionListener(e -> this.onClickSuggest());
        this.reset.addActionListener(e -> this.onClickReset());
    }

    public void addListener(final GridActionListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final GridActionListener listener) {
        this.listeners.remove(listener);
    }

    private void onClickHome() {
        this.listeners.forEach(GridActionListener::onHome);
    }

    private void onClickUndo() {
        this.listeners.forEach(GridActionListener::onUndo);
    }

    private void onClickSuggest() {
        this.listeners.forEach(GridActionListener::onSuggest);
    }

    private void onClickReset() {
        this.listeners.forEach(GridActionListener::onReset);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.home.refreshPalette(palette);
        this.undo.refreshPalette(palette);
        this.suggest.refreshPalette(palette);
        this.reset.refreshPalette(palette);
    }
}
