package view;

import javax.swing.*;
import java.awt.*;

public class SudokuUI extends JFrame {
    public static final String TITLE = "Sudoku Game";
    public static final Dimension FRAME_SIZE = new Dimension(800, 800);
    
    private final MenuPage menuPage;
    
    public SudokuUI() {
        super(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_SIZE);
        this.setLocationRelativeTo(null);
        
        this.menuPage = new MenuPage();
        this.setLayout(new FlowLayout());
        
        this.add(this.menuPage, BorderLayout.CENTER);
    }


    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final SudokuUI sudokuUI = new SudokuUI();
            sudokuUI.setVisible(true);
        });
    }
    
}
