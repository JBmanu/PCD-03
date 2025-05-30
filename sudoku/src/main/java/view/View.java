package view;

import model.Coordinate;
import model.Grid;
import view.color.Palette;
import view.listener.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static view.utils.StyleUtils.FRAME_SIZE;
import static view.utils.StyleUtils.TITLE_GUI;

public class View extends JFrame implements UI {
    private final MenuPage menuPage;
    private final GridPage gridPage;

    public View() {
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
    public void setSuggest(final Coordinate key, final Integer value) {
        this.gridPage.setSuggest(key, value);
    }

    @Override
    public void undo(final Coordinate coordinate) {
        this.gridPage.undo(coordinate);
    }

    @Override
    public void reset(final Map<Coordinate, Integer> resetGrid) {
        this.gridPage.reset(resetGrid);
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
    public void addNumberInfoListener(final NumberInfoListener listener) {
        this.gridPage.addNumberInfoListener(listener);
    }

    @Override
    public void addGridCellListener(final GridCellListener listener) {
        this.gridPage.addGridCellListener(listener);
    }

    @Override
    public void addGridCellInsertListener(final GridCellInsertListener listener) {
        this.gridPage.addGridCellInsertListener(listener);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.getContentPane().setBackground(palette.neutral());
        this.menuPage.refreshPalette(palette);
    }

}
