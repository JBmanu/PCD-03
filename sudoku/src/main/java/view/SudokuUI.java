package view;

import javax.swing.*;
import java.awt.*;

public class SudokuUI extends JFrame {
    public static final String TITLE = "Sudoku Game";
    public static final Dimension FRAME_SIZE = new Dimension(800, 600);
    
    
    

    public SudokuUI() {
        super(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_SIZE);
        this.setLocationRelativeTo(null);
    }


    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final SudokuUI sudokuUI = new SudokuUI();
            sudokuUI.setVisible(true);
        });
    }
    
}
