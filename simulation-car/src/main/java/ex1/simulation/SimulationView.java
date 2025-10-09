package ex1.simulation;

import ex1.inspector.InspectorPanelView;
import ex1.road.RoadPanelView;
import ex1.road.RoadsEnv;
import ex1.simulation.listener.ViewSimulationListener;
import ex1.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends JFrame implements ViewSimulationListener {
    private final InspectorPanelView inspectorPanelView;
    private final RoadPanelView roadPanelView;
    private final BorderLayout layoutManager;
    private final JPanel glassPane;

    public SimulationView(final SimulationManager simulationManager) {
        super("RoadSim View");
        this.setSize(ViewUtils.GUI_WIDTH, ViewUtils.GUI_HEIGHT);
        this.inspectorPanelView = new InspectorPanelView(simulationManager);
        this.roadPanelView = new RoadPanelView(ViewUtils.ROAD_WIDTH, ViewUtils.ROAD_HEIGHT);
        this.layoutManager = new BorderLayout();
        this.glassPane = new JPanel();

        this.setupGlassPane();
        this.setLayout(this.layoutManager);

        this.glassPane.add(this.inspectorPanelView, BorderLayout.NORTH);
        this.add(BorderLayout.CENTER, this.roadPanelView);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    private void setupGlassPane() {
        this.setGlassPane(this.glassPane);
        this.glassPane.setLayout(new BorderLayout());
        this.glassPane.setOpaque(false);
        this.glassPane.setVisible(true);
    }

    public void setupCommandsSimulation(final InspectorSimulation simulation) {
        this.inspectorPanelView.setupSimulation(simulation);
    }

    @Override
    public void notifyInit(final int t, final InspectorSimulation simulation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyStepDone(final int t, final InspectorSimulation simulation) {
        SwingUtilities.invokeLater(() -> {
            final var e = ((RoadsEnv) simulation.environment());
            this.roadPanelView.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
            this.inspectorPanelView.updateInspector(simulation);
        });
    }

    @Override
    public void notifyEnd(final InspectorSimulation simulation) {
        SwingUtilities.invokeLater(() -> {
            this.inspectorPanelView.endUpdateInspector(simulation);
        });
    }


}
