package ui.utils;

import javax.swing.*;
import java.awt.*;

public final class PanelUtils {

    public static void transparent(final JPanel panel) {
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
    }

    public static JPanel createTransparent() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    public static JPanel createTransparent(final LayoutManager layout) {
        final JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    public static JPanel createVertical() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    public static JPanel createHorizontal() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    public static JPanel createCenter() {
        return createTransparent(new FlowLayout(FlowLayout.CENTER, 0, 0));
    }

    public static JPanel createCenter(final int hGap, final int vGap) {
        return createTransparent(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
    }

    public static JPanel createCenter(final Component... components) {
        final JPanel panel = createCenter();
        for (final Component component : components) panel.add(component);
        return panel;
    }

    public static JPanel createCenterWithGap(final int hGap, final int vGap, final Component... components) {
        final JPanel panel = createTransparent(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
        for (final Component component : components) panel.add(component);
        return panel;
    }
    
    public static JPanel createVerticalGap(final int vGap, final String constraints, final Component... components) {
        final JPanel panel = createTransparent(new BorderLayout());
        final JPanel centerPanel = createCenter(components);

        panel.add(Box.createVerticalStrut(vGap), constraints);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    public static JPanel createNorthGap(final int vGap, final Component... components) {
        return createVerticalGap(vGap, BorderLayout.NORTH, components);
    }
    
    public static JPanel createSouthGap(final int vGap, final Component... components) {
        return createVerticalGap(vGap, BorderLayout.SOUTH, components);
    }
    
    public static JPanel createGrid(final int rows, final int cols) {
        final JPanel panel = new JPanel(new GridLayout(rows, cols));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }
}
