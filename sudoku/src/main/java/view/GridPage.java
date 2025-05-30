package view;

import model.Coordinate;
import model.Grid;
import view.color.Palette;
import view.components.ColorComponent;
import view.components.SNumberCell;
import view.listener.GridActionListener;
import view.listener.GridCellInsertListener;
import view.listener.GridCellListener;
import view.listener.NumberInfoListener;
import view.panel.GridActionPanel;
import view.panel.NumberInfoPanel;
import view.utils.PanelUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static view.utils.StyleUtils.*;

public class GridPage extends JPanel implements ColorComponent {
    private final JPanel gridPanel;
    private final Map<Coordinate, SNumberCell> cells;

    private final NumberInfoPanel numberInfoPanel;
    private final GridActionPanel gridActionPanel;

    private Color gridColor;

    public GridPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.gridPanel = PanelUtils.createTransparent();
        this.cells = new HashMap<>();

        this.numberInfoPanel = new NumberInfoPanel();
        this.gridActionPanel = new GridActionPanel();
        this.gridColor = Color.BLACK;

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

        final int quadrantSize = (int) Math.sqrt(grid.size());

        grid.orderedCells().forEach(entry -> {
            final SNumberCell cell = new SNumberCell(entry.getKey(), entry.getValue());
            this.cells.put(entry.getKey(), cell);
            this.gridPanel.add(cell);

            cell.setBorder(this.getCellBorder(entry.getKey().row(), entry.getKey().column(), grid.size(), quadrantSize,
                    CELL_BORDER, DIVISOR_BORDER));
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

    private Border getCellBorder(final int row, final int col, final int gridSize, final int quadrantSize, final int thin, final int thick) {
        int top = (row % quadrantSize == 0 && row != 0) ? thick : thin;
        int left = (col % quadrantSize == 0 && col != 0) ? thick : thin;
        int bottom = ((row + 1) % quadrantSize == 0 && row != gridSize - 1) ? thick : thin;
        int right = ((col + 1) % quadrantSize == 0 && col != gridSize - 1) ? thick : thin;
        if (row == 0) top = 0;
        if (col == 0) left = 0;
        if (row == gridSize - 1) bottom = 0;
        if (col == gridSize - 1) right = 0;

        return BorderFactory.createMatteBorder(top, left, bottom, right, this.gridColor);
    }

    public void addGridActionListener(final GridActionListener listener) {
        this.gridActionPanel.addListener(listener);
    }

    public void addNumberInfoListener(final NumberInfoListener listener) {
        this.numberInfoPanel.addListener(listener);
    }

    public void addGridCellListener(final GridCellListener listener) {
        this.cells.values().forEach(cell -> cell.addListener(listener));
    }

    public void addGridCellInsertListener(final GridCellInsertListener listener) {
        this.cells.values().forEach(cell -> cell.addInsertListeners(listener));
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.gridActionPanel.refreshPalette(palette);
        this.numberInfoPanel.refreshPalette(palette);
        this.gridColor = palette.secondaryWithAlpha(230);
//        this.cells.values().forEach(cell -> cell.refreshPalette(palette));
    }
}
