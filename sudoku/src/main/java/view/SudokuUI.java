package view;

import model.Grid;
import view.color.Palette;
import view.components.ColorComponent;
import view.listener.GridActionListener;
import view.listener.MenuListener;

import javax.swing.*;
import java.awt.*;

import static view.utils.StyleUtils.FRAME_SIZE;
import static view.utils.StyleUtils.TITLE_GUI;

public class SudokuUI extends JFrame implements UI, ColorComponent {
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
        this.showMenuPage();
    }
    
    private void showPage(final JPanel page) {
        this.getContentPane().removeAll();
        this.getContentPane().add(page, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        this.setVisible(false);
        this.dispose();
    }

    @Override
    public void showMenuPage() {
        this.showPage(this.menuPage);
    }
    
    @Override
    public void showGridPage() {
        this.showPage(this.gridPage);
    }

    @Override
    public void buildGrid(final Grid grid) {
        this.gridPage.build(grid);
    }

    @Override
    public void addMenuListener(final MenuListener listener) {
        this.menuPage.addListener(listener);
    }

    @Override
    public void addGridActionListener(final GridActionListener listener) {
        this.gridPage.addGridActionListener(listener);
    }
    
    @Override
    public void refreshPalette(final Palette palette) {
        
    }
    
}
