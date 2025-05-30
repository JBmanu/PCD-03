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
        final Colorable.Background background = Colorable.createBackground(
                palette.primary(),
                palette.primaryWithAlpha(150),
                palette.interaction(),
                palette.secondaryWithAlpha(50));
        
        final Colorable.Text text = Colorable.createText(
                palette.neutral(),
                palette.neutral(),
                palette.neutral());
                
        this.colorable.setBackground(background);
        this.colorable.setText(text);
        this.repaint();
    }
}
