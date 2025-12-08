package ui.multiPlayer;

import grid.Settings;
import ui.color.Palette;
import ui.components.*;
import ui.listener.MenuPageListener;
import ui.listener.ThemeInvoke;
import ui.multiPlayer.panel.InfoPanel;
import ui.utils.PanelUtils;
import utils.ConditionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static ui.utils.PathUtils.ICON_START;
import static ui.utils.StyleUtils.*;

public class MenuMultiplayerPage extends JPanel implements InfoPanel, ColorComponent, ThemeInvoke {

    private static final String EXIT = "Exit";
    private static final String START_GAME = "Start Game";
    private static final String DARK_MODE = "Dark Mode";
    private static final String LIGHT_MODE = "Light Mode";

    private final SSelector<Settings.Schema> schemaSelector;
    private final SSelector<Settings.Difficulty> difficultySelector;

    private final PlaceholderTextField roomNameField;
    private final PlaceholderTextField playerNameField;
    private final SButton startGameButton;
    private final SButton themeModeButton;
    private final SButton exitButton;
    private final JLabel errorLabel;

    private final List<MenuPageListener> listeners;
    private final List<GameMultiplayerListener.StartListener> startListeners;

    private boolean isDarkMode;
    private final Map<Boolean, Runnable> themeActions;

    public MenuMultiplayerPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);
        this.listeners = new ArrayList<>();
        this.startListeners = new ArrayList<>();

        this.roomNameField = new PlaceholderTextField("Room Name");
        this.playerNameField = new PlaceholderTextField("Player Name");
        final SImage icon = new SImage(ICON_START, DIMENSION_ICON_MENU);
        this.startGameButton = new SButton(START_GAME);
        this.themeModeButton = new SButton(DARK_MODE);
        this.exitButton = new SButton(EXIT);
        this.schemaSelector = new SSelector<>(Arrays.stream(Settings.Schema.values()).toList());
        this.difficultySelector = new SSelector<>(Arrays.stream(Settings.Difficulty.values()).toList());
        this.errorLabel = new JLabel();

        this.isDarkMode = false;
        this.themeActions = ConditionUtils.createBoolean(this::invokeDarkMode, this::invokeLightMode);

        final List<PlaceholderTextField> textFields = List.of(this.roomNameField, this.playerNameField);
        final List<SSelector<?>> selectors = List.of(this.schemaSelector, this.difficultySelector);
        final List<SButton> buttons = List.of(this.startGameButton, this.themeModeButton, this.exitButton);
        final List<JComponent> allComponents = new ArrayList<>(Stream.concat(textFields.stream(),
                selectors.stream().map(selector -> (JComponent) selector)).toList());
        allComponents.addAll(buttons);
        allComponents.add(this.errorLabel);

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
        final Optional<String> roomName = this.roomNameField.getText().isEmpty() ?
                Optional.empty() : Optional.of(this.roomNameField.getText());
        final Optional<String> playerName = this.playerNameField.getText().isEmpty() ?
                Optional.empty() : Optional.of(this.playerNameField.getText());

        this.startListeners.forEach(l -> l.onStart(
                roomName, playerName, this.schemaSelector.item(), this.difficultySelector.item()));
    }

    private void onThemeMode() {
        this.themeActions.get(this.isDarkMode = !this.isDarkMode).run();
    }

    public void onExit() {
        this.listeners.forEach(MenuPageListener::onExit);
        this.startListeners.forEach(GameMultiplayerListener.StartListener::onExit);
    }

    public void addListener(final MenuPageListener listener) {
        this.listeners.add(listener);
    }

    public void addStartListener(final GameMultiplayerListener.StartListener listener) {
        this.startListeners.add(listener);
    }

    @Override
    public boolean isDarkMode() {
        return this.isDarkMode;
    }

    @Override
    public void invokeLightMode() {
        this.isDarkMode = false;
        this.themeModeButton.setText(LIGHT_MODE);
        this.listeners.forEach(MenuPageListener::onLightMode);
    }

    @Override
    public void invokeDarkMode() {
        this.isDarkMode = true;
        this.themeModeButton.setText(DARK_MODE);
        this.listeners.forEach(MenuPageListener::onDarkMode);
    }

    @Override
    public void showInfo(final String info) {
        this.errorLabel.setForeground(Color.BLACK);
        this.errorLabel.setText(info);
    }

    @Override
    public void showError(final String error) {
        this.errorLabel.setForeground(Color.RED);
        this.errorLabel.setText(error);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.roomNameField.refreshPalette(palette);
        this.playerNameField.refreshPalette(palette);
        this.schemaSelector.refreshPalette(palette);
        this.difficultySelector.refreshPalette(palette);
        this.startGameButton.refreshPalette(palette);
        this.themeModeButton.refreshPalette(palette);
        this.exitButton.refreshPalette(palette);
        this.errorLabel.setForeground(palette.secondary());
    }
}
