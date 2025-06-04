package ui.panel;

import ui.color.Colorable;
import ui.color.Palette;
import ui.components.ColorComponent;
import ui.components.SButton;
import ui.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static ui.utils.StyleUtils.*;

public class NumberInfoPanel extends JPanel implements ColorComponent {
    private final Colorable colorable;
    private final Map<Integer, SButton> numbers;

    public NumberInfoPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, ZERO_GAP));
        PanelUtils.transparent(this);

        this.numbers = new HashMap<>();
        this.colorable = Colorable.test();
    }

    public void setup(final int length) {
        this.removeAll();
        this.numbers.clear();

        for (int i = 1; i <= length; i++) {
            final SButton numberButton = new SButton(i + "");
            numberButton.setPreferredSize(DIMENSION_BUTTON_INFO);
            numberButton.setBorder(BorderFactory.createEmptyBorder());
            numberButton.setFont(INFO_FONT);
            numberButton.setFocusable(Boolean.FALSE);
            this.numbers.put(i, numberButton);
            this.add(numberButton);
        }

        this.numbers.values().forEach(button -> button.setColorable(this.colorable));
    }

    public void checkNumber(final int value, final int size, final int count) {
        this.numbers.entrySet().stream().filter(entry -> entry.getKey() == value)
                .findFirst()
                .ifPresent(entry -> {
                    final SButton button = entry.getValue();
                    if (count == size) button.setVisible(false);
                });
    }

    @Override
    public void refreshPalette(final Palette palette) {
        final Colorable colorable = Colorable.createSecondaryButton(palette);
        this.colorable.setBackground(colorable.background());
        this.colorable.setText(colorable.text());
        this.numbers.values().forEach(button -> button.setColorable(colorable));
    }
}
