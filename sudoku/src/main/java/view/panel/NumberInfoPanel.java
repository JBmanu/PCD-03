package view.panel;

import view.components.SButton;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.*;
import static view.utils.StyleUtils.INFO_FONT;

public class NumberInfoPanel extends JPanel {
    private final List<SButton> numbers;
    
    public NumberInfoPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));
        PanelUtils.transparent(this);
        
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
            this.add(numberButton);
        }
    }
    
    
}
