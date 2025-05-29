package view;

import model.Settings;
import utils.ConditionUtils;
import view.color.Palette;
import view.components.ColorComponent;
import view.components.SButton;
import view.components.SImage;
import view.components.SSelector;
import view.listener.MenuListener;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static view.utils.PathUtils.ICON_START;
import static view.utils.StyleUtils.*;

public class MenuPage extends JPanel implements ColorComponent {

    private static final String EXIT = "Exit";
    private static final String START_GAME = "Start Game";
    private static final String DARK_MODE = "Dark Mode";
    private static final String LIGHT_MODE = "Light Mode";

    private final SSelector<Settings.Schema> schemaSelector;
    private final SSelector<Settings.Difficulty> difficultySelector;

    private final SButton startGameButton;
    private final SButton themeModeButton;
    private final SButton exitButton;

    private final List<MenuListener> listeners;

    private boolean isDarkMode;
    private final Map<Boolean, Runnable> themeActions;

    public MenuPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);
        this.listeners = new ArrayList<>();

        final SImage icon = new SImage(ICON_START, DIMENSION_ICON_MENU);
        this.startGameButton = new SButton(START_GAME);
        this.themeModeButton = new SButton(DARK_MODE);
        this.exitButton = new SButton(EXIT);
        this.schemaSelector = new SSelector<>(Arrays.stream(Settings.Schema.values()).toList());
        this.difficultySelector = new SSelector<>(Arrays.stream(Settings.Difficulty.values()).toList());

        this.isDarkMode = true;
        this.themeActions = ConditionUtils.createBoolean(
                () -> this.listeners.forEach(MenuListener::onLightMode),
                () -> this.listeners.forEach(MenuListener::onDarkMode));

        final List<SSelector<?>> selectors = List.of(this.schemaSelector, this.difficultySelector);
        final List<SButton> buttons = List.of(this.startGameButton, this.themeModeButton, this.exitButton);
        final List<JComponent> allComponents = Stream.concat((selectors.stream().map(selector -> (JComponent) selector)),
                buttons.stream()).toList();

        final JPanel panel = PanelUtils.createVertical();
        allComponents.forEach(component -> component.setFont(FONT_GAME));
        allComponents.forEach(component -> component.setPreferredSize(DIMENSION_BUTTON_MENU));
        allComponents.forEach(component -> panel.add(PanelUtils.createCenterWithGap(ZERO_GAP, V_GAP, component)));

        this.add(icon, BorderLayout.NORTH);
        this.add(PanelUtils.createCenter(panel), BorderLayout.CENTER);

        this.startGameButton.addActionListener(_ -> this.onStartGame());
        this.themeModeButton.addActionListener(_ -> this.onThemeMode());
        this.exitButton.addActionListener(_ -> this.onExit());
    }

    private void onStartGame() {
        this.listeners.forEach(l -> l.onStart(
                this.schemaSelector.getSelectedItem(),
                this.difficultySelector.getSelectedItem()));
    }

    private void onThemeMode() {
        this.themeActions.get(this.isDarkMode = !this.isDarkMode).run();
    }

    private void onExit() {
        this.listeners.forEach(MenuListener::onExit);
    }

    public void addListener(final MenuListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final MenuListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.schemaSelector.refreshPalette(palette);
        this.difficultySelector.refreshPalette(palette);
        
        this.startGameButton.refreshPalette(palette);
        this.themeModeButton.refreshPalette(palette);
        this.exitButton.refreshPalette(palette);
    }
}
