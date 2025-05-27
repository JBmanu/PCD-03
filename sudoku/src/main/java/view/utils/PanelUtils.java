package view.utils;

import javax.swing.*;
import java.awt.*;

public final class PanelUtils {
    
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

    public static JPanel createCenter() {
        return createTransparent(new FlowLayout(FlowLayout.CENTER, 0, 0));
    }

    public static JPanel createCenter(final Component... components) {
        final JPanel panel = createCenter();
        for (final Component component : components) panel.add(component);
        return panel;
    }
}
