package ex1.inspector;

import ex1.simulation.InspectorSimulation;
import ex1.simulation.SimulationManager;
import ex1.simulation.SimulationType;
import ex1.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;

public class InspectorSimulationView extends JPanel implements StartStopViewListener {
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
            final JComboBox<SimulationType> comboBox = (JComboBox<SimulationType>) e.getSource();
            final SimulationType selectedOption = (SimulationType) comboBox.getSelectedItem();
            if (selectedOption == null) return;
            System.out.println("Opzione selezionata: " + selectedOption);
            simulationManager.initSimulation(selectedOption);
        });
    }

    @Override
    public boolean conditionToStart(final InspectorSimulation simulation) {
        return true;
    }

    @Override
    public void onStart(final InspectorSimulation simulation) {

    }

    @Override
    public void reset(final InspectorSimulation simulation) {

    }
}
