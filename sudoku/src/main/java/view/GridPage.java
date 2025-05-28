package view;

import model.Coordinate;
import model.Grid;
import model.Settings;
import view.components.SButton;
import view.components.SNumberCell;
import view.panel.GridActionPanel;
import view.panel.NumberInfoPanel;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static view.utils.StyleUtils.*;

public class GridPage extends JPanel {
    private final JLabel title;

    private final JPanel gridPanel;
    private final Map<Coordinate, SNumberCell> cells;

//    private final JPanel numberInfoPanel;
//    private final List<SButton> numbers;

    private NumberInfoPanel numberInfoPanel;
    private GridActionPanel gridActionPanel;


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

        this.build(Grid.create(Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY)));
    }


    public void build(final Grid grid) {
        this.cells.clear();
        this.gridPanel.removeAll();
        this.gridPanel.setLayout(new GridLayout(grid.size(), grid.size()));

        grid.orderedCells().forEach(entry -> {
            final SNumberCell cell = new SNumberCell(entry.getKey(), entry.getValue());
            this.cells.put(entry.getKey(), cell);
            this.gridPanel.add(cell);
        });

        this.numberInfoPanel.setup(grid.size());
    }


}
