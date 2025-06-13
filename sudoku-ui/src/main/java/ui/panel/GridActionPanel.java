package ui.panel;

import ui.color.Palette;
import ui.components.ColorComponent;
import ui.components.SButton;
import ui.listener.GameListener;
import ui.listener.GridPageListener;
import ui.listener.ThemeInvoke;
import ui.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.utils.StyleUtils.*;

public class GridActionPanel extends JPanel implements ColorComponent {
    public static final String DARK = "Dark";
    public static final String LIGHT = "Light";
    private final ThemeInvoke themeInvoker;
    private final List<GridPageListener.ActionListener> listeners;
    private final List<GameListener.ActionListener> actionListeners;

    private final SButton home;
    private final SButton undo;
    private final SButton suggest;
    private final SButton reset;
    private final SButton theme;

    public GridActionPanel(final ThemeInvoke themeInvoker) {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, ZERO_GAP));
        PanelUtils.transparent(this);

        this.themeInvoker = themeInvoker;
        this.listeners = new ArrayList<>();
        this.actionListeners = new ArrayList<>();

        this.home = new SButton("Home");
        this.undo = new SButton("Undo");
        this.suggest = new SButton("Suggest");
        this.reset = new SButton("Reset");
        this.theme = new SButton("Light");

        final List<SButton> buttons = List.of(this.home, this.undo, this.suggest, this.reset, this.theme);
        buttons.forEach(button -> button.setFont(FONT_GAME));
        buttons.forEach(this::add);

        this.home.addActionListener(_ -> this.onClickHome());
        this.undo.addActionListener(_ -> this.onClickUndo());
        this.suggest.addActionListener(_ -> this.onClickSuggest());
        this.reset.addActionListener(_ -> this.onClickReset());
        this.theme.addActionListener(_ -> this.onClickTheme());
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
    
    private void refreshThemeButton() {
        this.theme.setText(this.themeInvoker.isDarkMode() ? DARK : LIGHT);
    }

    private void onClickTheme() {
        if (this.themeInvoker.isDarkMode())
            this.onLightTheme();
        else 
            this.onDackTheme();
    }

    private void onLightTheme() {
        this.theme.setText(LIGHT);
        this.themeInvoker.invokeLightMode();
    }
    
    private void onDackTheme() {
        this.theme.setText(DARK);
        this.themeInvoker.invokeDarkMode();
    }

    public void addListener(final GridPageListener.ActionListener listener) {
        this.listeners.add(listener);
    }

    public void addActionListener(final GameListener.ActionListener listener) {
        this.actionListeners.add(listener);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.refreshThemeButton();
        this.home.refreshPalette(palette);
        this.undo.refreshPalette(palette);
        this.suggest.refreshPalette(palette);
        this.reset.refreshPalette(palette);
        this.theme.refreshPalette(palette);
    }
}
