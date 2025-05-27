package view;

import model.Coordinate;
import view.components.SNumberCell;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GridPage extends JPanel {
    
    private final Map<Coordinate, SNumberCell> cells;
    
    public GridPage() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

//        final JPanel gridPanel = PanelUtils.createTransparent(new GridLayout(9, 9));
        GridLayout layout = new GridLayout(9, 9);
        final JPanel gridPanel = new JPanel(layout);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final Coordinate position = Coordinate.create(row, col);
                final SNumberCell cell = new SNumberCell(position, row);
                gridPanel.add(cell);
            }
        }
        this.cells = new HashMap<>();
        gridPanel.setBackground(Color.green);
        
        
        this.add(gridPanel, BorderLayout.CENTER);
        
    }
    
    
}
