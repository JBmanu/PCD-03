package view;

import view.components.JSelector;
import view.components.SButton;
import view.components.SImage;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static view.utils.PathUtils.ICON_START;
import static view.utils.StyleUtils.*;

public class MenuPage extends JPanel {

    private static final String EXIT = "Exit";
    private static final String START_GAME = "Start Game";
    private static final String DARK_MODE = "Dark Mode";
    private static final String LIGHT_MODE = "Light Mode";

    private final JSelector<String> difficultyComboBox;
    private final JSelector<String> gridSizeComboBox;

    private final SButton startGameButton;
    private final SButton exitButton;
    private final SButton darkModeButton;

    public MenuPage() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        final SImage icon = new SImage(ICON_START, DIMENSION_ICON_START);
        this.startGameButton = new SButton(START_GAME);
        this.exitButton = new SButton(EXIT);
        this.darkModeButton = new SButton(DARK_MODE);
        this.difficultyComboBox = new JSelector<>(List.of("Easy", "Medium", "Hard"));
        this.gridSizeComboBox = new JSelector<>(List.of("4x4", "6x6", "9x9"));

        final List<JSelector<String>> selectors = List.of(this.difficultyComboBox, this.gridSizeComboBox);
        final List<SButton> buttons = List.of(this.startGameButton, this.darkModeButton, this.exitButton);
        final List<JComponent> allComponents = Stream.concat((selectors.stream().map(selector -> (JComponent) selector)),
                buttons.stream()).toList();

        allComponents.forEach(component -> component.setFont(FONT_GAME));
        allComponents.forEach(component -> component.setPreferredSize(DIMENSION_BUTTON_MENU));

        final JPanel panel = PanelUtils.createVertical();

        selectors.forEach(panel::add);
        buttons.forEach(button -> panel.add(PanelUtils.createCenter(button)));

        this.add(icon, BorderLayout.NORTH);
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(panel, BorderLayout.CENTER);
        this.add(Box.createHorizontalGlue(), BorderLayout.WEST);
    }

}
