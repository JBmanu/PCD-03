package view.inspector.setting;

import simulation.SimulationManager;
import simulation.SimulationType;
import view.ViewUtils;

import javax.swing.*;
import java.awt.*;

public class InspectorSimulationView extends JPanel {
    private final DefaultComboBoxModel<SimulationType> comboBoxModel;
    private final JComboBox<SimulationType> comboBox;

    public InspectorSimulationView(final SimulationManager simulationManager) {
        this.comboBoxModel = new DefaultComboBoxModel<>();
        this.comboBoxModel.addElement(SimulationType.SINGLE_ROAD);
        this.comboBoxModel.addElement(SimulationType.SINGLE_ROAD_TRAFFIC_LIGHT);
        this.comboBoxModel.addElement(SimulationType.CROSSROAD_TRAFFIC_LIGHT);
        this.comboBox = new JComboBox<>(this.comboBoxModel);

        this.setLayout(new FlowLayout());
        this.add(this.comboBox);
        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);
        this.setOpaque(false);

        this.comboBox.setSelectedItem(simulationManager.simulationType());
        this.comboBox.addActionListener(e -> {
            final JComboBox<SimulationType> cb = (JComboBox<SimulationType>) e.getSource();
            final SimulationType selectedOption = (SimulationType) cb.getSelectedItem();
            if (selectedOption == null) return;
            simulationManager.changeSimulation(selectedOption);
//            SwingUtilities.invokeLater(() -> cb.setSelectedIndex(-1));
        });
    }
}