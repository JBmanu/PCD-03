package ex1.simulation;

public class SimulationManager {
    private SimulationType simulationType;
    private final SimulationView view;

    public SimulationManager() {
        this.simulationType = SimulationType.SINGLE_ROAD;
        this.view = new SimulationView(this);
        this.initSimulation(this.simulationType);
    }

    public SimulationType simulationType() {
        return this.simulationType;
    }

    public void initSimulation(final SimulationType simulationType) {
        this.simulationType = simulationType;
        final AbstractSimulation simulation = simulationType.getSimulation();
        simulation.addViewListener(this.view);
        this.view.setupCommandsSimulation(simulation);
    }


}
