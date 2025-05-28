package view.panel;

import view.components.SButton;
import view.listener.NumberInfoListener;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.*;

public class NumberInfoPanel extends JPanel {

    private final List<NumberInfoListener> listeners;
    private final List<SButton> numbers;

    public NumberInfoPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));
        PanelUtils.transparent(this);

        this.listeners = new ArrayList<>();
        this.numbers = new ArrayList<>();
    }

    public void setup(final int length) {
        this.removeAll();
        this.numbers.clear();

        for (int i = 1; i <= length; i++) {
            final SButton numberButton = new SButton(i + "");
            numberButton.setPreferredSize(DIMENSION_BUTTON_INFO);
            numberButton.setBorder(BorderFactory.createEmptyBorder());
            numberButton.setFont(INFO_FONT);
            this.numbers.add(numberButton);
            this.add(numberButton);
        }

        this.numbers.forEach(button -> button.addActionListener(_ -> this.onClickNumber(button)));
    }


    public void addListener(final NumberInfoListener listener) {
        this.listeners.add(listener);
    }

    public void addRemove(final NumberInfoListener listener) {
        this.listeners.remove(listener);
    }


    private void onClickNumber(final SButton button) {
        try {
            final int number = Integer.parseInt(button.getText());
            this.listeners.forEach(l -> l.onSelected(number));
        } catch (final NumberFormatException e) {
            System.err.println("Invalid number format: " + button.getText());
        }
    }


}
