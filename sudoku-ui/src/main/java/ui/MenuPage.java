package ui;

import grid.Settings;
import utils.ConditionUtils;
import ui.color.Palette;
import ui.components.ColorComponent;
import ui.components.SButton;
import ui.components.SImage;
import ui.components.SSelector;
import ui.listener.GameListener;
import ui.listener.MenuPageListener;
import ui.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ui.utils.PathUtils.ICON_START;
import static ui.utils.StyleUtils.*;

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

    private final List<MenuPageListener> listeners;
    private final List<GameListener.StartListener> startListeners;

    private boolean isDarkMode;
    private final Map<Boolean, Runnable> themeActions;

    public MenuPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);
        this.listeners = new ArrayList<>();
        this.startListeners = new ArrayList<>();

        final SImage icon = new SImage(ICON_START, DIMENSION_ICON_MENU);
        this.startGameButton = new SButton(START_GAME);
        this.themeModeButton = new SButton(DARK_MODE);
        this.exitButton = new SButton(EXIT);
        this.schemaSelector = new SSelector<>(Arrays.stream(Settings.Schema.values()).toList());
        this.difficultySelector = new SSelector<>(Arrays.stream(Settings.Difficulty.values()).toList());

        this.isDarkMode = true;
        this.themeActions = ConditionUtils.createBoolean(
                () -> this.listeners.forEach(MenuPageListener::onLightMode),
                () -> this.listeners.forEach(MenuPageListener::onDarkMode));

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

        this.schemaSelector.addActionListener(() -> this.listeners.forEach(MenuPageListener::onChangeScheme));
        this.difficultySelector.addActionListener(() -> this.listeners.forEach(MenuPageListener::onChangeDifficulty));
        this.startGameButton.addActionListener(_ -> this.onStartGame());
        this.themeModeButton.addActionListener(_ -> this.onThemeMode());
        this.exitButton.addActionListener(_ -> this.onExit());
    }

    private void onStartGame() {
        this.listeners.forEach(MenuPageListener::onStart);
        this.startListeners.forEach(l -> l.onStart(
                this.schemaSelector.item(), this.difficultySelector.item()));
    }

    private void onThemeMode() {
        this.themeActions.get(this.isDarkMode = !this.isDarkMode).run();
        this.themeModeButton.setText(this.isDarkMode ? DARK_MODE : LIGHT_MODE);
    }

    private void onExit() {
        this.listeners.forEach(MenuPageListener::onExit);
        this.startListeners.forEach(GameListener.StartListener::onExit);
    }

    public void addListener(final MenuPageListener listener) {
        this.listeners.add(listener);
    }

    public void addStartListener(final GameListener.StartListener listener) {
        this.startListeners.add(listener);
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
