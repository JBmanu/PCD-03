package view.components;

import view.color.ColorComponent;
import view.color.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JSelector<T> extends JPanel implements ColorComponent {
    //    private final List<SelectorListener> listeners;
    private final JMyButton rightButton;
    private final JMyButton leftButton;
    private final JLabel label;
    private final List<T> items;
    private int index = 0;

    public JSelector(final List<T> items) {
        this.rightButton = new JMyButton(">");
        this.leftButton = new JMyButton("<");
//        this.listeners = new ArrayList<>();
        this.label = new JLabel();
        this.items = items;

        this.updateLabel();
        this.rightButton.addActionListener(e -> {
            this.onRightButtonClick();
//            this.listeners.forEach(SelectorListener::onRightButtonClick);
        });
        this.leftButton.addActionListener(e -> {
            this.onLeftButtonClick();
//            this.listeners.forEach(SelectorListener::onLeftButtonClick);
        });

        this.label.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.setOpaque(false);

        this.add(this.leftButton);
        this.add(this.label);
        this.add(this.rightButton);

//        this.refreshColor();
    }

//    public void addSelectorListener(final SelectorListener listener) {
//        this.listeners.add(listener);
//    }

    private void updateLabel() {
        this.label.setText(this.items.get(this.index).toString());
    }

    private void onLeftButtonClick() {
        this.index = (this.index - 1 + this.items.size()) % this.items.size();
        this.updateLabel();
    }

    private void onRightButtonClick() {
        this.index = (this.index + 1) % this.items.size();
        this.updateLabel();
    }

    public T getSelectedItem() {
        return this.items.get(this.index);
    }

    public void setFontSelection(final Font font) {
        this.label.setFont(font);
        this.leftButton.setFont(font);
        this.rightButton.setFont(font);
    }

    @Override
    public void setPreferredSize(final Dimension dimension) {
        super.setPreferredSize(dimension);
        this.label.setPreferredSize(new Dimension(dimension.width / 2, dimension.height));
        this.leftButton.setPreferredSize(new Dimension((dimension.width / 4), dimension.height));
        this.rightButton.setPreferredSize(new Dimension((dimension.width / 4), dimension.height));
    }

    @Override
    public void refreshPalette(final Palette palette) {
        this.label.setForeground(palette.secondary());
        this.leftButton.refreshPalette(palette);
        this.rightButton.refreshPalette(palette);
        this.repaint();
    }
}
