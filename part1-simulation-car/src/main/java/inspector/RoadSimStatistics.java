package inspector;

import car.CarAgent;
import simulation.InspectorSimulation;
import simulation.listener.ModelSimulationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements ModelSimulationListener {
    private final List<Double> carsSpeed;
    private double currentAverageSpeed;
    private double averageSpeed;
    private double minSpeed;
    private double maxSpeed;

    public RoadSimStatistics() {
        this.carsSpeed = new ArrayList<>();
    }

    public void addCarSpeed(final CarAgent carAgent) {
        final double currSpeed = carAgent.getCurrentSpeed();
        this.carsSpeed.add(currSpeed);
        if (currSpeed > this.maxSpeed) {
            this.maxSpeed = currSpeed;
        } else if (currSpeed < this.minSpeed) {
            this.minSpeed = currSpeed;
        }
    }

    public double currentAverageSpeed() {
        return this.currentAverageSpeed;
    }

    public double averageSpeed() {
        return this.averageSpeed;
    }

    public double minSpeed() {
        return this.minSpeed;
    }

    public double maxSpeed() {
        return this.maxSpeed;
    }

    @Override
    public void notifyInit(final int t, final InspectorSimulation simulation) {
        this.averageSpeed = 0;
        this.currentAverageSpeed = 0;
        this.maxSpeed = Double.MIN_VALUE;
        this.minSpeed = Double.MAX_VALUE;
    }

    @Override
    public void notifyStepDone(final int t, final InspectorSimulation simulation) {
        this.currentAverageSpeed = this.carsSpeed.stream().mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        this.averageSpeed += this.currentAverageSpeed;
        this.carsSpeed.clear();
    }

    @Override
    public void notifyEnd(final InspectorSimulation simulation) {
        final int totalStep = simulation.engine().totalStep();
        if (totalStep > 0) this.averageSpeed /= totalStep;
    }

    private void log(final String msg) {
        System.out.println("[STAT] " + msg);
    }

}
