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

    
public class View extends JFrame implements UI, MenuPageListener, GridActionListener {
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

        this.menuPage.addListener(this);
        this.gridPage.addListener(this);

        this.showMenuPage();
    }

    private void showPage(final JPanel page) {
        this.getContentPane().removeAll();
        this.getContentPane().add(page, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private void showMenuPage() {
        this.showPage(this.menuPage);
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
    public void showGridPage() {
        this.showPage(this.gridPage);
    }

    @Override
    public void addPlayerListener(final GameListener.PlayerListener listener) {
        this.menuPage.addStartListener(listener);
        this.gridPage.addActionListener(listener);
    }

    @Override
    public void buildGrid(final Grid grid) {
        this.gridPage.build(grid);
    }

    @Override
    public void suggest(final Coordinate key, final Integer value) {
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
    public void onStart() {

    }

    @Override
    public void onExit() {
        
    }

    @Override
    public void onLightMode() {
        this.refreshPalette(Palette.light());
    }

    @Override
    public void onDarkMode() {
        this.refreshPalette(Palette.dark());
    }

    @Override
    public void onHome() {
        this.showMenuPage();
    }

    @Override
    public void onUndo() {
    
    }

    @Override
    public void onSuggest() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.getContentPane().setBackground(palette.neutral());
        this.menuPage.refreshPalette(palette);
        this.gridPage.refreshPalette(palette);
    }
}
