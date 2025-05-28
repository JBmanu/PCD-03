package view;

import model.Coordinate;
import model.Grid;
import model.Settings;
import view.components.SNumberCell;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GridPage extends JPanel {

    private final Map<Coordinate, SNumberCell> cells;
    private final JPanel gridPanel;

    public GridPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.gridPanel = new JPanel();
        this.cells = new HashMap<>();

        this.build(Grid.create(Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY)));

        this.add(this.gridPanel, BorderLayout.CENTER);
    }

    public void build(final Grid grid) {
        this.cells.clear();
        this.gridPanel.removeAll();

        this.gridPanel.setLayout(new GridLayout(grid.size(), grid.size()));
        this.gridPanel.setBackground(Color.green);

        
        grid.orderedCells().forEach(entry -> {
            final SNumberCell cell = new SNumberCell(entry.getKey(), entry.getValue());
            this.cells.put(entry.getKey(), cell);
            this.gridPanel.add(cell);
        });
    }


}
