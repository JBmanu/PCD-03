package view.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class RoundButtonUI extends BasicButtonUI {
    @Override
    public void installUI(final JComponent c) {
        super.installUI(c);
        final AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

    }

}
