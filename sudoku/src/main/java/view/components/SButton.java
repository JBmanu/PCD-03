package view.components;

import view.color.ColorEvent;
import view.color.Colorable;
import view.color.Palette;

import javax.swing.*;
import java.awt.*;

public class SButton extends JButton implements ColorComponent {
    private final Colorable colorable;
    private int arcSize;

    public SButton(final String text) {
        super(text);
        super.setOpaque(false);
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);

        this.arcSize = 10;
        this.colorable = Colorable.test();
        ColorEvent.create(this.colorable, this);
    }
    
    public void setArcSize(final int arcSize) {
        this.arcSize = arcSize;
    }
    
    public void setColorable(final Colorable colorable) {
        this.colorable.setBackground(colorable.background());
        this.colorable.setText(colorable.text());
        this.repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        this.colorable.currentBackground().ifPresent(g2::setColor);
        this.colorable.currentText().ifPresent(this::setForeground);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), this.arcSize, this.arcSize);
        super.paintComponent(g);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        final Colorable colorable = Colorable.createMainButton(palette);
        this.colorable.setBackground(colorable.background());
        this.colorable.setText(colorable.text());
        this.repaint();
    }
}
