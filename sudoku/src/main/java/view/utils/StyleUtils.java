package view.utils;

import java.awt.*;

public final class StyleUtils {

    // TITLE
    public static final String TITLE_GUI = "Sudoku";

    // CELL
    public static final int SIZE_CELL = 60;
    public static final float SIZE_CELL_FONT = 24.0f;
    // CELL BORDER
    public static final int DIVISOR_BORDER = 4;
    public static final int CELL_BORDER = 1;

    // GENERAL
    public static final Font FONT_GAME = new Font("Roboto", Font.PLAIN, 20);

    // START GAME AREA
    public static final Dimension DIMENSION_ICON_START = new Dimension(300, 300);
    public static final Dimension DIMENSION_CONTAINER_START_BTN = new Dimension(250, 300);
    public static final Dimension DIMENSION_BUTTON_START = new Dimension(250, 50);
    public static final int SPACE_LEFT_RIGHT_START_GAME_AREA = 200;
    public static final int SPACE_TOP_BOTTOM_START_GAME_AREA = 100;

    // SUDOKU AREA
    public static final Dimension SIZE_BUTTON_SUDOKU = new Dimension(140, 50);
    public static final int SPACE_LEFT_RIGHT_GRID = 150;
    public static final int SPACE_COMMAND_AREA = 10;
    // INFO
    public static final Dimension DIMENSION_BUTTON_INFO = new Dimension(40, 40);
    public static final Font INFO_FONT = new Font("Roboto", Font.BOLD, 20);
    public static final int H_GAP_INFO = 15;
    public static final int V_GAP_INFO = 30;
    
}
