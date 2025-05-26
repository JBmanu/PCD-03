package view.components;

import view.color.ColorComponent;
import view.color.Colorable;
import view.color.Palette;
import view.listener.NumberListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JMyButton extends JButton implements ColorComponent {

    private final Colorable colorable;

    private boolean isHover;
    private int arcSize;

    private final List<NumberListener> listeners;

    public JMyButton(final String text) {
        super(text);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.listeners = new ArrayList<>();
        this.colorable = Colorable.test();
        this.arcSize = 10;

        this.addActionListener(e -> this.listeners.forEach(listener -> {
            try {
                listener.onSelectNumber(Optional.of(Integer.parseInt(this.getText())));
            } catch (final NumberFormatException ex) {
                listener.onSelectNumber(Optional.empty());
            }
        }));

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final MouseEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    JMyButton.this.colorable.setClick();
                    JMyButton.this.repaint();
                });
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JMyButton.this.colorable.setHover();
                    JMyButton.this.repaint();
                });
            }

            public void mouseEntered(final MouseEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    JMyButton.this.colorable.setHover();
                    JMyButton.this.repaint();
                });
            }

            public void mouseExited(final MouseEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    JMyButton.this.colorable.setDefault();
                    JMyButton.this.repaint();
                });
            }
        });
    }

    public JMyButton(final int value) {
        this(String.valueOf(value));
    }

    public void addHoverListener(final NumberListener listener) {
        this.listeners.add(listener);
    }

    public void addAllHoverListener(final List<NumberListener> listeners) {
        this.listeners.addAll(listeners);
    }

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

//    @Override
//    protected void paintBorder(final Graphics g) {
//        this.setBorder(null);
//    }

    @Override
    public void updateUI() {
        this.setUI(new RoundButtonUI());
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.isHover = false;

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
