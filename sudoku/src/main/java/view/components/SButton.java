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

//    public JMyButton(final int value) {
//        this(String.valueOf(value));
//    }

//    public void addHoverListener(final NumberListener listener) {
//        this.listeners.add(listener);
//    }
//
//    public void addAllHoverListener(final List<NumberListener> listeners) {
//        this.listeners.addAll(listeners);
//    }

//    public void setBgColorDisabled(final Color color) {
//        this.bgColorDisabled = color;
//    }
//
//    public void setGbColorHover(final Color color) {
//        this.gbColorHover = color;
//    }
//
//    public void setBgColorDefault(final Color color) {
//        this.bgColorDefault = color;
//    }
//
//    public void setBgColorOnClick(final Color color) {
//        this.bgColorOnClick = color;
//    }

//    public void setFgColorDefault(final Color color) {
//        this.fgColorDefault = color;
//        this.setForeground(color);
//    }

//    public void setFgColorHover(final Color color) {
//        this.fgColorHover = color;
//    }

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
//        this.gbColorHover = palette.primary();
//        this.bgColorOnClick = palette.secondary();
//        this.bgColorDefault = palette.third();
//
//        this.bgColorDisabled = COLOR_SCHEME.disabled();
//
//        this.fgColorDefault = COLOR_SCHEME.second();
//        this.fgColorHover = COLOR_SCHEME.third();
        this.repaint();
    }
}
