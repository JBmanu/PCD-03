package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(final String placeholder) {
        this.placeholder = placeholder;
        this.setOpaque(false);

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
            final Insets insets = this.getInsets();
            g2.drawString(this.placeholder, insets.left + 2, this.getHeight() / 2 + this.getFont().getSize() / 2 - 2);
            g2.dispose();
        }
    }
}