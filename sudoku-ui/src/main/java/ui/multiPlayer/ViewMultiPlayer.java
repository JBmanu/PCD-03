package ui.multiPlayer;

import grid.Coordinate;
import grid.Grid;
import ui.color.Palette;
import ui.listener.GameListener;
import ui.listener.GridPageListener;
import ui.listener.MenuPageListener;
import ui.sound.SoundManager;
import ui.sound.Track;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import static ui.utils.StyleUtils.FRAME_SIZE;
import static ui.utils.StyleUtils.TITLE_GUI;


public class ViewMultiPlayer extends JFrame implements UIMultiplayer, MenuPageListener, GridPageListener.ActionListener, GameListener.ActionListener {
    private final SoundManager backgroundSoundManager;
    private final SoundManager effectSoundManager;
    private final MenuMultiplayerPage menuPage;
    private final GridMultiplayerPage gridPage;

    public ViewMultiPlayer() {
        super(TITLE_GUI);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_SIZE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.blue);

        this.backgroundSoundManager = SoundManager.createBackground();
        this.effectSoundManager = SoundManager.createEffect();
        this.menuPage = new MenuMultiplayerPage();
        this.gridPage = new GridMultiplayerPage(this.menuPage);

        this.menuPage.addListener(this);
        this.gridPage.addActionListener(this);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                ViewMultiPlayer.this.menuPage.onExit();
            }
        });
        
        this.refreshPalette(Palette.light());
        this.showMenuPage();
    }

    private void showPage(final JPanel page) {
        this.getContentPane().removeAll();
        this.getContentPane().add(page, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private void showMenuPage() {
        this.showInfo("");
        this.showPage(this.menuPage);
        this.backgroundSoundManager.playSound(Track.SoundBG.START);
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
        this.backgroundSoundManager.playSound(Track.SoundBG.SUDOKU);
        this.showPage(this.gridPage);
    }

    @Override
    public void addPlayerListener(final GameMultiplayerListener.PlayerListener listener) {
        this.menuPage.addStartListener(listener);
        this.gridPage.addPlayerListener(listener);
    }

    @Override
    public void buildGrid(final Grid grid) {
        this.gridPage.build(grid);
    }

    @Override
    public void buildRoom(final String roomId) {
        this.gridPage.buildRoom(roomId);
    }

    @Override
    public void joinPlayer(final String newPlayerName) {
        this.gridPage.joinPlayer(newPlayerName);
    }

    @Override
    public void leavePlayer(final String playerName) {
        this.gridPage.leavePlayer(playerName);
    }

    @Override
    public void appendPlayers(final List<String> currentPlayers) {
        this.gridPage.appendPlayers(currentPlayers);
    }

    @Override
    public void suggest(final Coordinate key, final Integer value) {
        this.gridPage.suggest(key, value);
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
    public void writeValueWithoutCheck(final Coordinate coordinate, final Integer value) {
        SwingUtilities.invokeLater(() -> this.gridPage.setValueWithoutCheck(coordinate, value));
    }

    @Override
    public void win(final String message) {
        JOptionPane.showMessageDialog(this, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
        this.showMenuPage();
    }

    @Override
    public void onChangeScheme() {
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onChangeDifficulty() {
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }
    
    @Override
    public void onStart() {
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onExit() {
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onLightMode() {
        this.refreshPalette(Palette.light());
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onDarkMode() {
        this.refreshPalette(Palette.dark());
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onHome() {
        this.showMenuPage();
        this.effectSoundManager.playSound(Track.SoundFX.CLICK);
    }

    @Override
    public void onUndo() {
        this.effectSoundManager.playSound(Track.SoundFX.RESET);
    }

    @Override
    public void onSuggest() {
        this.effectSoundManager.playSound(Track.SoundFX.SUGGEST);
    }

    @Override
    public void onReset() {
        this.effectSoundManager.playSound(Track.SoundFX.RESET);
    }

    @Override
    public void showInfo(final String info) {
        this.menuPage.showInfo(info);
        this.gridPage.showInfo(info);
    }

    @Override
    public void showError(final String error) {
        this.menuPage.showError(error);
        this.gridPage.showError(error);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.getContentPane().setBackground(palette.neutral());
        this.menuPage.refreshPalette(palette);
        this.gridPage.refreshPalette(palette);
    }
}
