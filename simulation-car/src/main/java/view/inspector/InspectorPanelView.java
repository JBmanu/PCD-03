package view.inspector;

import akka.actor.typed.ActorRef;
import simulation.InspectorSimulation;
import simulation.SimulationManager;
import view.ViewUtils;
import view.inspector.setting.InspectorSimulationView;
import view.inspector.setting.StartStopView;
import actors.SimulationActor;

import javax.swing.*;
import java.awt.*;

public class InspectorPanelView extends JPanel {
    private final StartStopView startStopView;
    private final StepperView stepperView;
    private final TimeStatisticsView timeStatisticsView;
    private final RoadStatisticView roadStatisticView;
    private final InspectorSimulationView simulationView;

    public InspectorPanelView(final SimulationManager simulationManager) {
        this.startStopView = new StartStopView(simulationManager);
        this.stepperView = new StepperView(simulationManager);
        this.timeStatisticsView = new TimeStatisticsView();
        this.roadStatisticView = new RoadStatisticView();
        this.simulationView = new InspectorSimulationView(simulationManager);

        this.startStopView.setStepperView(this.stepperView);

        this.setLayout(new BorderLayout());
        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);
        this.setupGraphics();
    }

    private void setupGraphics() {
        final JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(this.startStopView);
        centerPanel.add(this.stepperView);

        this.add(centerPanel, BorderLayout.NORTH);

        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setOpaque(false);
        rightPanel.add(this.simulationView);
        this.add(rightPanel, BorderLayout.EAST);

        final FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        final JPanel westPanel = new JPanel(flowLayout);
        flowLayout.setHgap(15);
        westPanel.setOpaque(false);
        westPanel.add(this.timeStatisticsView);
        westPanel.add(this.roadStatisticView);
        this.add(westPanel, BorderLayout.WEST);

        this.setOpaque(false);
    }

    public void setupSimulation(final ActorRef<SimulationActor.Command> simulation) {
        // non serve più — ci pensa SimulationManager
    }

    public void updateInspector(final InspectorSimulation simulation) {
        this.stepperView.updateStepper(simulation.engine().currentStep());
        this.timeStatisticsView.updateStatistics(simulation.engine());
        this.roadStatisticView.updateStatistics(simulation.roadStatistics());
    }

    public void onIdle() {
        this.startStopView.onIdle();
        this.stepperView.onIdle();
    }

    public void onRunning() {
        this.startStopView.onRunning();
        this.stepperView.onRunning();
    }

    public void onPaused() {
        this.startStopView.onPaused();
    }

    public void onEnded() {
        this.startStopView.onEnded();
    }
}