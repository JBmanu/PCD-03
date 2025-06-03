package view;

import model.Coordinate;
import model.Grid;
import view.color.Palette;
import view.components.ColorComponent;
import view.components.SNumberCell;
import view.listener.GameListener;
import view.listener.GridPageListener;
import view.panel.GridActionPanel;
import view.panel.NumberInfoPanel;
import view.utils.GridUtils;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static view.utils.StyleUtils.*;

public class GridPage extends JPanel implements ColorComponent, GridPageListener.CellListener {
    private final JPanel gridPanel;
    private final Map<Coordinate, SNumberCell> cells;

    private final NumberInfoPanel numberInfoPanel;
    private final GridActionPanel gridActionPanel;

    private final List<GameListener.CellListener> cellListeners;

    private Optional<Palette> optionPalette;
    private Color gridColor;

    public GridPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.gridPanel = PanelUtils.createTransparent();
        this.cells = new HashMap<>();

        this.numberInfoPanel = new NumberInfoPanel();
        this.gridActionPanel = new GridActionPanel();
        this.optionPalette = Optional.empty();
        this.gridColor = Color.BLACK;

        this.cellListeners = new ArrayList<>();

        final JPanel interactionPanel = PanelUtils.createTransparent(new BorderLayout());
        interactionPanel.add(this.gridActionPanel, BorderLayout.NORTH);
        interactionPanel.add(Box.createVerticalStrut(V_GAP + V_GAP), BorderLayout.CENTER);
        interactionPanel.add(this.numberInfoPanel, BorderLayout.SOUTH);

        final int height = V_GAP * 4;
        this.add(Box.createVerticalStrut(height), BorderLayout.NORTH);
        this.add(this.gridPanel, BorderLayout.CENTER);
        this.add(PanelUtils.createCenterWithGap(ZERO_GAP, height, interactionPanel), BorderLayout.SOUTH);
        this.add(Box.createHorizontalStrut(H_GAP + H_GAP), BorderLayout.WEST);
        this.add(Box.createHorizontalStrut(H_GAP + H_GAP), BorderLayout.EAST);
    }


    public void build(final Grid grid) {
        this.cells.clear();
        this.gridPanel.removeAll();
        this.gridPanel.setLayout(new GridLayout(grid.size(), grid.size()));

        grid.orderedCells().forEach(entry -> {
            final SNumberCell cell = new SNumberCell(entry.getKey(), entry.getValue());
            this.cells.put(entry.getKey(), cell);
            cell.setColorable(this.optionPalette);
            cell.addListener(this);
            cell.addInsertListeners(this);
            this.cellListeners.forEach(cell::addCellListeners);
            this.gridPanel.add(cell);

            cell.setBorder(GridUtils.getCellBorder(entry.getKey().row(), entry.getKey().column(), grid.size(),
                    CELL_BORDER, DIVISOR_BORDER, this.gridColor));
        });

        this.numberInfoPanel.setup(grid.size());
    }

    public void setSuggest(final Coordinate key, final Integer value) {
        this.cells.get(key).setSuggest(value);
    }

    public void undo(final Coordinate coordinate) {
        this.cells.get(coordinate).undo();
    }

    public void reset(final Map<Coordinate, Integer> resetGrid) {
        resetGrid.forEach((coordinate, value) -> this.cells.get(coordinate).setSuggest(value));
    }

    public void addListener(final GridPageListener.ActionListener listener) {
        this.gridActionPanel.addListener(listener);
    }

    public void addActionListener(final GameListener.PlayerListener listener) {
        this.gridActionPanel.addActionListener(listener);
        this.cellListeners.add(listener);
    }

    @Override
    public void onFocusGainedCell(final SNumberCell cell) {
        if (cell.value().isEmpty()) return;
        final Coordinate coordinate = cell.coordinate();
        final int size = (int) Math.sqrt(this.cells.size());

        final List<Coordinate> coordinates = Stream.concat(GridUtils.createRowAndColFrom(coordinate, size).stream(),
                GridUtils.computeQuadrant(coordinate, size).stream()).toList();
        
        coordinates.stream().map(this.cells::get).forEach(SNumberCell::colorOnHelper);

        this.cells.values().stream().filter(cellGrid -> cellGrid.value().equals(cell.value()))
                .forEach(SNumberCell::colorOnHint);

        cell.colorOnSelected();
    }

    @Override
    public void onFocusLostCell(final SNumberCell cell) {
        this.cells.values().forEach(SNumberCell::colorOnUnselected);
    }

    @Override
    public void onChangeCell(final SNumberCell cell) {
        this.onFocusGainedCell(cell);
    }

    @Override
    public void onRemoveCell(final SNumberCell cell) {
        this.onFocusLostCell(cell);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.gridActionPanel.refreshPalette(palette);
        this.numberInfoPanel.refreshPalette(palette);

        final int alpha = 230;
        this.gridColor = palette.secondaryWithAlpha(alpha);
        this.optionPalette = Optional.of(palette);
    }
}
