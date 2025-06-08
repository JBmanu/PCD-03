package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(final String placeholder) {
        this.placeholder = placeholder;
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder());

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
        super.paintComponent(g);

        if (this.placeholder != null && this.getText().isEmpty() && !this.isFocusOwner()) {
            final Graphics2D g2 = (Graphics2D) g.create();

            g2.setFont(this.getFont().deriveFont(Font.ITALIC));
            g2.setColor(Color.GRAY);

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
}