package ui.multiPlayer;

import grid.Coordinate;
import grid.Grid;
import ui.color.Palette;
import ui.components.ColorComponent;
import ui.components.SNumberCell;
import ui.listener.GameListener;
import ui.listener.GridPageListener;
import ui.listener.ThemeInvoke;
import ui.multiPlayer.panel.GameInfoPanel;
import ui.multiPlayer.panel.InfoPanel;
import ui.panel.GridActionPanel;
import ui.panel.NumberInfoPanel;
import ui.utils.BorderUtils;
import ui.utils.PanelUtils;
import utils.GridUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static ui.utils.StyleUtils.*;

public class GridMultiplayerPage extends JPanel implements ColorComponent, GridPageListener.CellListener, InfoPanel {
    private final JPanel gridPanel;
    private final Map<Coordinate, SNumberCell> cells;

    private final GameInfoPanel gameInfoPanel;
    private final NumberInfoPanel numberInfoPanel;
    private final GridActionPanel gridActionPanel;

    private final List<GameListener.CellListener> cellListeners;

    private Optional<Palette> optionPalette;
    private Color gridColor;

    public GridMultiplayerPage(final ThemeInvoke themeInvoker) {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.gridPanel = PanelUtils.createTransparent();
        this.cells = new HashMap<>();

        this.gameInfoPanel = new GameInfoPanel();
        this.numberInfoPanel = new NumberInfoPanel();
        this.gridActionPanel = new GridActionPanel(themeInvoker);
        this.optionPalette = Optional.empty();
        this.gridColor = Color.BLACK;

        this.cellListeners = new ArrayList<>();

        final JPanel interactionPanel = PanelUtils.createTransparent(new BorderLayout());
        interactionPanel.add(this.gridActionPanel, BorderLayout.NORTH);
        interactionPanel.add(Box.createVerticalStrut(V_GAP + V_GAP), BorderLayout.CENTER);
        interactionPanel.add(this.numberInfoPanel, BorderLayout.SOUTH);

        final int height = V_GAP * 4;
        this.add(Box.createVerticalStrut(height), BorderLayout.NORTH);
        this.add(this.gameInfoPanel, BorderLayout.WEST);
        this.add(this.gridPanel, BorderLayout.CENTER);
        this.add(PanelUtils.createCenterWithGap(ZERO_GAP, height, interactionPanel), BorderLayout.SOUTH);
        this.add(Box.createHorizontalStrut(H_GAP + H_GAP), BorderLayout.EAST);
    }


    public void build(final Grid grid) {
        SwingUtilities.invokeLater(() -> {
            this.cells.clear();
            this.gridPanel.removeAll();
            this.gridPanel.setLayout(new GridLayout(grid.size(), grid.size()));

            grid.orderedCells().forEach(entry -> {
                final SNumberCell cell = new SNumberCell(entry.getKey(), entry.getValue());
                this.cells.put(entry.getKey(), cell);
                this.optionPalette.ifPresent(cell::refreshPalette);
                cell.addInsertListeners(this);
                cell.addSelectionListener(this);
                this.cellListeners.forEach(cell::addCellListeners);
                this.gridPanel.add(cell);
                cell.setBorder(BorderUtils.create(cell, grid.size(), CELL_BORDER, DIVISOR_BORDER, this.gridColor));
            });
            this.gridPanel.revalidate();

            this.numberInfoPanel.setup(grid.size());
            for (int i = 1; i <= grid.size();
                 this.numberInfoPanel.checkNumber(i, grid.size(), this.countValue(i++)))
                ;
        });
    }

    public void buildRoom(final String roomId) {
        this.gameInfoPanel.buildRoom(roomId);
    }

    public void joinPlayer(final String newPlayerName) {
        this.gameInfoPanel.joinPlayer(newPlayerName);
    }

    public void leavePlayer(final String playerName) {
        this.gameInfoPanel.leavePlayer(playerName);
    }

    public void appendPlayers(final List<String> currentPlayers) {
        this.gameInfoPanel.appendPlayers(currentPlayers);
    }

    public void suggest(final Coordinate key, final Integer value) {
        this.cells.get(key).setSuggest(value);
        this.cells.values().forEach(SNumberCell::unselectedColor);
    }

    public void undo(final Coordinate coordinate) {
        final SNumberCell cell = this.cells.get(coordinate);
        cell.value().ifPresent(value -> {
            final int size = (int) Math.sqrt(this.cells.size());
            cell.undo();
            this.numberInfoPanel.checkNumber(value, size, this.countValue(value));
        });
    }

    public void reset(final Map<Coordinate, Integer> resetGrid) {
        resetGrid.forEach((coordinate, value) -> this.cells.get(coordinate).setSuggest(value));
        this.numberInfoPanel.reset();
    }

    public void addActionListener(final GridPageListener.ActionListener listener) {
        this.gridActionPanel.addListener(listener);
    }

    public void addPlayerListener(final GameMultiplayerListener.PlayerListener listener) {
        this.gridActionPanel.addActionListener(listener);
        this.cellListeners.add(listener);
    }

    private List<SNumberCell> cellsOf(final int value) {
        return this.cells.values().stream()
                .filter(cell -> cell.value().isPresent() && cell.value().get() == value)
                .toList();
    }

    public void setValueWithoutCheck(final Coordinate coordinate, final Integer value) {
        SwingUtilities.invokeLater(() -> {
            final int size = (int) Math.sqrt(this.cells.size());
            final SNumberCell cell = this.cells.get(coordinate);
            cell.setValueWithoutCheck(value);
            this.numberInfoPanel.checkNumber(value, size, this.countValue(value));
        });
    }

    @Override
    public void onFocusGainedCell(final SNumberCell cell) {
        if (cell.value().isEmpty()) {
            this.onFocusLostCell(cell);
            return;
        }
        final Coordinate coordinate = cell.coordinate();
        final int size = (int) Math.sqrt(this.cells.size());
        final List<Coordinate> coordinates = Stream.concat(GridUtils.createRowAndColFrom(coordinate, size).stream(),
                GridUtils.computeQuadrant(coordinate, size).stream()).toList();

        coordinates.stream().map(this.cells::get).forEach(SNumberCell::helpColor);
        cell.value().ifPresent(value -> this.cellsOf(value).forEach(SNumberCell::hintColor));
        cell.selectionColor();
    }

    @Override
    public void onFocusLostCell(final SNumberCell cell) {
        this.cells.values().forEach(SNumberCell::unselectedColor);
    }

    private int countValue(final int value) {
        return (int) this.cells.values().stream()
                .filter(cell -> cell.value().isPresent() && cell.value().get() == value).count();
    }

    @Override
    public void onModifyCell(final SNumberCell cell) {
        this.onFocusGainedCell(cell);
        cell.value().ifPresent(value -> {
            final int size = (int) Math.sqrt(this.cells.size());
            this.numberInfoPanel.checkNumber(value, size, this.countValue(value));
        });
    }

    @Override
    public void showInfo(final String info) {
        this.gameInfoPanel.showInfo(info);
    }

    @Override
    public void showError(final String error) {
        this.gameInfoPanel.showError(error);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.gridActionPanel.refreshPalette(palette);
        this.numberInfoPanel.refreshPalette(palette);
        this.gameInfoPanel.refreshPalette(palette);
        this.cells.values().forEach(cell -> cell.refreshPalette(palette));
        
        final int alpha = 230;
        this.gridColor = palette.secondaryWithAlpha(alpha);
        this.optionPalette = Optional.of(palette);
    }
}
