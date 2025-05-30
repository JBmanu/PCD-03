package view.components;

import view.color.Colorable;
import view.color.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static view.utils.StyleUtils.ZERO_GAP;

public class SSelector<T> extends JPanel implements ColorComponent {
    private final SButton rightButton;
    private final SButton leftButton;
    private final JLabel label;
    private final List<T> items;
    private int index = 0;

    public SSelector(final List<T> items) {
        this.items = items;
        this.label = new JLabel("");
        this.rightButton = new SButton(">");
        this.leftButton = new SButton("<");

        final List<SButton> buttons = List.of(this.rightButton, this.leftButton);
        buttons.forEach(button -> button.setBorder(BorderFactory.createEmptyBorder()));

        this.updateLabel();
        this.rightButton.addActionListener(e -> this.onRightButtonClick());
        this.leftButton.addActionListener(e -> this.onLeftButtonClick());

        this.label.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, ZERO_GAP, ZERO_GAP));
        this.setOpaque(false);

        this.add(this.leftButton);
        this.add(this.label);
        this.add(this.rightButton);
    }

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

    @Override
    public void setFont(final Font font) {
        if (Objects.nonNull(this.label)) this.label.setFont(font);
        if (Objects.nonNull(this.leftButton)) this.leftButton.setFont(font);
        if (Objects.nonNull(this.rightButton)) this.rightButton.setFont(font);
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
        final int alpha = 230;
        this.label.setForeground(palette.secondaryWithAlpha(alpha));
        this.label.setBackground(palette.neutral());
        this.leftButton.refreshPalette(palette);
        this.rightButton.refreshPalette(palette);
        this.repaint();
    }
}
