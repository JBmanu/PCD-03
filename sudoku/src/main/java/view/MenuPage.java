package view;

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
import java.util.List;
import java.util.stream.Stream;

import static view.utils.PathUtils.ICON_START;
import static view.utils.StyleUtils.*;

public class MenuPage extends JPanel implements ColorComponent {

    private static final String EXIT = "Exit";
    private static final String START_GAME = "Start Game";
    private static final String DARK_MODE = "Dark Mode";
    private static final String LIGHT_MODE = "Light Mode";

    private final SSelector<String> difficultyComboBox;
    private final SSelector<String> gridSizeComboBox;

    private final SButton startGameButton;
    private final SButton themeModeButton;
    private final SButton exitButton;
    
    private final List<MenuListener> listeners;

    public MenuPage() {
        this.setLayout(new BorderLayout());
        PanelUtils.transparent(this);
        this.listeners = new ArrayList<>();

        final SImage icon = new SImage(ICON_START, DIMENSION_ICON_START);
        this.startGameButton = new SButton(START_GAME);
        this.themeModeButton = new SButton(DARK_MODE);
        this.exitButton = new SButton(EXIT);
        this.difficultyComboBox = new SSelector<>(List.of("Easy", "Medium", "Hard"));
        this.gridSizeComboBox = new SSelector<>(List.of("4x4", "6x6", "9x9"));

        final List<SSelector<String>> selectors = List.of(this.difficultyComboBox, this.gridSizeComboBox);
        final List<SButton> buttons = List.of(this.startGameButton, this.themeModeButton, this.exitButton);
        final List<JComponent> allComponents = Stream.concat((selectors.stream().map(selector -> (JComponent) selector)),
                buttons.stream()).toList();

        
        
        final JPanel panel = PanelUtils.createVertical();
        allComponents.forEach(component -> component.setFont(FONT_GAME));
        allComponents.forEach(component -> component.setPreferredSize(DIMENSION_BUTTON_MENU));
        allComponents.forEach(component -> panel.add(PanelUtils.createCenterWithGap(ZERO_GAP, V_GAP, component)));

        this.add(icon, BorderLayout.NORTH);
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(panel, BorderLayout.CENTER);
        this.add(Box.createHorizontalGlue(), BorderLayout.WEST);
        
        this.startGameButton.addActionListener(_ -> this.onStartGame());
        this.themeModeButton.addActionListener(_ -> this.onThemeMode());
        this.exitButton.addActionListener(_ -> this.onExit());
        
    }
    
    private void onStartGame() {
        
    }
    
    private void onThemeMode() {
        this.listeners.forEach(MenuListener::onDarkMode);
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

    }
}
