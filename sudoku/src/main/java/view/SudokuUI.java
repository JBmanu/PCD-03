package view;

import view.color.Palette;
import view.components.ColorComponent;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;

import static view.utils.StyleUtils.FRAME_SIZE;
import static view.utils.StyleUtils.TITLE_GUI;

public class SudokuUI extends JFrame implements ColorComponent {
    private final MenuPage menuPage;
    private final GridPage gridPage;
    
    public SudokuUI() {
        super(TITLE_GUI);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_SIZE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.blue);
        
        this.menuPage = new MenuPage();
        this.gridPage = new GridPage();
        
        this.add(this.gridPage, BorderLayout.CENTER);
    }
    
    @Override
    public void refreshPalette(final Palette palette) {
        
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final SudokuUI sudokuUI = new SudokuUI();
            sudokuUI.setVisible(true);
        });
    }
}
