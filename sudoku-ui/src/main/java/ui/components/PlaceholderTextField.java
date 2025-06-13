package ui.components;

import ui.color.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static ui.utils.StyleUtils.ARC_SIZE;

public class PlaceholderTextField extends JTextField implements ColorComponent {
    private Color borderColor;
    private Color backgroundColor;
    private Color placeholderColor;
    private String placeholder;

    public PlaceholderTextField(final String placeholder) {
        this.borderColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.placeholderColor = Color.GRAY;
        this.placeholder = placeholder;
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                PlaceholderTextField.this.repaint();
            }

            @Override
            public void focusLost(final FocusEvent e) {
                PlaceholderTextField.this.repaint();
            }
        });
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
        this.repaint();
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo manuale (perché opaque = false)
        g2.setColor(this.backgroundColor);
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), ARC_SIZE, ARC_SIZE);

        // Bordo
        g2.setColor(this.borderColor);
        g2.setStroke(new BasicStroke(3.f));
        g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, ARC_SIZE, ARC_SIZE);
        
        super.paintComponent(g);

        if (this.placeholder != null && this.getText().isEmpty()) {
//            final Graphics2D g2 = (Graphics2D) g.create();

            g2.setFont(this.getFont().deriveFont(Font.ITALIC));
            g2.setColor(this.placeholderColor);

            final FontMetrics fm = g2.getFontMetrics();
            final int textWidth = fm.stringWidth(this.placeholder);
            final int textHeight = fm.getAscent();

            // Calcolo centro orizzontale e verticale
            final int x = (this.getWidth() - textWidth) / 2;
            final int y = (this.getHeight() + textHeight) / 2 - 2;

            g2.drawString(this.placeholder, x, y);
            g2.dispose();
        }

    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.backgroundColor = palette.neutral();
        
        this.borderColor = palette.primary();
        this.placeholderColor = palette.secondaryWithAlpha(150);
        
        this.setForeground(palette.secondary());
        this.setCaretColor(palette.secondary());
    }
}