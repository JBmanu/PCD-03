package view.utils;

import java.awt.*;

public final class StyleUtils {

    // TITLE
    public static final String TITLE_GUI = "Sudoku";

    // CELL
    public static final float SIZE_CELL_FONT = 24.0f;
    public static final int DIVISOR_BORDER = 2;
    public static final int CELL_BORDER = 1;

    // GENERAL
    public static final Dimension FRAME_SIZE = new Dimension(800, 800);
    
    public static final Font FONT_TITLE = new Font("Roboto", Font.BOLD, 40);
    public static final Font FONT_GAME = new Font("Roboto", Font.PLAIN, 20);
    public static final Font INFO_FONT = new Font("Roboto", Font.BOLD, 20);
    
    public static final int ZERO_GAP = 0;
    public static final int V_GAP = 5;
    public static final int H_GAP = 10;

    // START GAME AREA
    public static final Dimension DIMENSION_ICON_START = new Dimension(300, 300);
    public static final Dimension DIMENSION_BUTTON_MENU = new Dimension(250, 50);

    // SUDOKU AREA
    public static final Dimension SIZE_BUTTON_SUDOKU = new Dimension(140, 50);
    public static final int SPACE_LEFT_RIGHT_GRID = 150;
    public static final int SPACE_COMMAND_AREA = 10;

    // INFO
    public static final Dimension DIMENSION_BUTTON_INFO = new Dimension(40, 40);

}
