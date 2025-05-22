package view;

import view.components.JMyButton;
import view.utils.PanelUtils;

import javax.swing.*;

import java.awt.*;

import static view.utils.StyleUtils.FONT_GAME;

public class MenuPage extends JPanel {

    private static final String EXIT = "Exit";
    private static final String START_GAME = "Start Game";
    private static final String DARK_MODE = "Dark Mode";
    private static final String LIGHT_MODE = "Light Mode";

    //    private final JSelector<Difficulty> difficultyComboBox;
//    private final JSelector<GridScheme> gridSizeComboBox;
    private final JMyButton startGameButton;
    private final JMyButton exitButton;
    private final JMyButton darkModeButton;

    public MenuPage() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.startGameButton = new JMyButton(START_GAME);
        this.exitButton = new JMyButton(EXIT);
        this.darkModeButton = new JMyButton(DARK_MODE);

        this.exitButton.setFont(FONT_GAME);
        this.startGameButton.setFont(FONT_GAME);
        this.darkModeButton.setFont(FONT_GAME);

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(this.startGameButton);
        panel.add(this.darkModeButton);
        panel.add(this.exitButton);
        
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(panel, BorderLayout.CENTER);
        this.add(Box.createGlue(), BorderLayout.WEST);

    }


}
