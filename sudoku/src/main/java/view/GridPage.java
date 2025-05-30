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
    private final JLabel title;

    private final JPanel gridPanel;
    private final Map<Coordinate, SNumberCell> cells;

    private final NumberInfoPanel numberInfoPanel;
    private final GridActionPanel gridActionPanel;

    public GridPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.title = new JLabel(TITLE_GUI);
        this.title.setFont(FONT_TITLE);
        final JPanel titlePanel = PanelUtils.createNorthGap(V_GAP, this.title);

        this.gridPanel = PanelUtils.createTransparent();
        this.cells = new HashMap<>();

        this.numberInfoPanel = new NumberInfoPanel();
        this.gridActionPanel = new GridActionPanel();

        final JPanel interactionPanel = PanelUtils.createTransparent(new BorderLayout());
        final JPanel gapInteractionPanel = PanelUtils.createSouthGap(V_GAP, interactionPanel);
        interactionPanel.add(this.gridActionPanel, BorderLayout.NORTH);
        interactionPanel.add(this.numberInfoPanel, BorderLayout.SOUTH);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(this.gridPanel, BorderLayout.CENTER);
        this.add(gapInteractionPanel, BorderLayout.SOUTH);
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

            cell.setBorder(GridPage.getCellBorder(
                    entry.getKey().row(), entry.getKey().column(), grid.size(), quadrantSize,
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

    public static Border getCellBorder(final int row, final int col, final int gridSize, final int quadrantSize, final int thin, final int thick) {
        int top = (row % quadrantSize == 0 && row != 0) ? thick : thin;
        int left = (col % quadrantSize == 0 && col != 0) ? thick : thin;
        int bottom = ((row + 1) % quadrantSize == 0 && row != gridSize - 1) ? thick : thin;
        int right = ((col + 1) % quadrantSize == 0 && col != gridSize - 1) ? thick : thin;
        if (row == 0) top = 0;
        if (col == 0) left = 0;
        if (row == gridSize - 1) bottom = 0;
        if (col == gridSize - 1) right = 0;

        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.green);
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
//        this.numberInfoPanel.refreshPalette(palette);
//        this.cells.values().forEach(cell -> cell.refreshPalette(palette));
    }
}
