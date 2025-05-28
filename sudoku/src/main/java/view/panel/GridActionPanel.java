package view.panel;

import view.components.SButton;
import view.listener.GridActionListener;
import view.utils.PanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.utils.StyleUtils.*;

public class GridActionPanel extends JPanel {
    private final List<GridActionListener> listeners;
    private final SButton home;
    private final SButton undo;
    private final SButton suggest;
    private final SButton reset;


    public GridActionPanel() {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));
        PanelUtils.transparent(this);

        this.listeners = new ArrayList<>();
        this.home = new SButton("Home");
        this.undo = new SButton("Undo");
        this.suggest = new SButton("Suggest");
        this.reset = new SButton("reset");

        final List<SButton> buttons = List.of(this.home, this.undo, this.suggest, this.reset);
        buttons.forEach(button -> button.setFont(FONT_GAME));
        buttons.forEach(this::add);
    }
}
