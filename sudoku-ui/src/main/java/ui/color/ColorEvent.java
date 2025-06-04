package ui.color;

import utils.ConditionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public interface ColorEvent {

    static ColorEvent create(final Colorable colorable, final Component component) {
        return new ColorEventImpl(colorable, component);
    }


    final class ColorMouseAdapter extends MouseAdapter {
        private final Colorable colorable;
        private final Component component;

        public ColorMouseAdapter(final Colorable colorable, final Component component) {
            this.colorable = colorable;
            this.component = component;
            this.component.addMouseListener(this);
        }

        public void mousePressed(final MouseEvent evt) {
            SwingUtilities.invokeLater(() -> {
                this.colorable.setClick();
                this.component.repaint();
            });
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            SwingUtilities.invokeLater(() -> {
                this.colorable.setHover();
                this.component.repaint();
            });
        }

        public void mouseEntered(final MouseEvent evt) {
            SwingUtilities.invokeLater(() -> {
                this.colorable.setHover();
                this.component.repaint();
            });
        }

        public void mouseExited(final MouseEvent evt) {
            SwingUtilities.invokeLater(() -> {
                this.colorable.setDefault();
                this.component.repaint();
            });
        }
    }

    final class ColorEnableComponentEvent {
        public static final String ENABLED = "enabled";
        private final Component component;
        private final Colorable colorable;
        private final ColorMouseAdapter colorMouseAdapter;

        public ColorEnableComponentEvent(final Colorable colorable, final Component component, final ColorMouseAdapter colorMouseAdapter) {
            this.colorable = colorable;
            this.component = component;
            this.colorMouseAdapter = colorMouseAdapter;

            component.addPropertyChangeListener(ENABLED, evt -> {
                final boolean enable = (boolean) evt.getNewValue();
                final Map<Boolean, Runnable> actions = ConditionUtils.createBoolean(this::setEnabled, this::setDisabled);
                actions.get(enable).run();
            });
        }

        private void setEnabled() {
            this.component.addMouseListener(this.colorMouseAdapter);
            this.colorable.setDefault();
        }

        private void setDisabled() {
            this.component.removeMouseListener(this.colorMouseAdapter);
            this.colorable.setDisabled();
        }
    }

    final class ColorEventImpl implements ColorEvent {

        public ColorEventImpl(final Colorable colorable, final Component component) {
            final ColorMouseAdapter colorMouseAdapter = new ColorMouseAdapter(colorable, component);
            final ColorEnableComponentEvent colorEnableComponentEvent = new ColorEnableComponentEvent(colorable, component, colorMouseAdapter);
        }
    }
}
