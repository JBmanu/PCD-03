package view.utils;

import javax.swing.*;
import java.awt.*;

public class PanelUtils {
    
    public static JPanel createTransparent(final LayoutManager layout) {
        final JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    } 
}
