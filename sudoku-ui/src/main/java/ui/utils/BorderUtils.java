package ui.utils;

import ui.components.SNumberCell;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public final class BorderUtils {


    public static Border create(final int row, final int col, final int gridSize,
                                final int thin, final int thick, final Color color) {
        final int quadrantSize = (int) Math.sqrt(gridSize);
        int top = (row % quadrantSize == 0 && row != 0) ? thick : thin;
        int left = (col % quadrantSize == 0 && col != 0) ? thick : thin;
        int bottom = ((row + 1) % quadrantSize == 0 && row != gridSize - 1) ? thick : thin;
        int right = ((col + 1) % quadrantSize == 0 && col != gridSize - 1) ? thick : thin;
//        final int margin = thick + thick;
        final int margin = 0;
        if (row == 0) top = margin;
        if (col == 0) left = margin;
        if (row == gridSize - 1) bottom = margin;
        if (col == gridSize - 1) right = margin;

        return BorderFactory.createMatteBorder(top, left, bottom, right, color);
    }
    
    public static Border create(final SNumberCell cell, final int gridSize,
                                final int thin, final int thick, final Color color) {
        final int row = cell.coordinate().row();
        final int col = cell.coordinate().column();
        
        return create(row, col, gridSize, thin, thick, color);
    }
    
    public static <T extends Border> void changeColor(final JComponent component, final Class<T> borderClass, final Color color) {
        SwingUtilities.invokeLater(() -> {
            final Border border = component.getBorder();
            try {
                final T newBorder = borderClass.getDeclaredConstructor(new Class[]{
                        Insets.class, Color.class}).newInstance(border.getBorderInsets(component), color);
                component.setBorder(newBorder);
                component.repaint();
            } catch (final InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }  
        });
        

    }
}
