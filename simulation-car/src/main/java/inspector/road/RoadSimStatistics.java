package inspector.road;

import car.CarAgent;
import simulation.InspectorSimulation;
import simulation.listener.ModelSimulationListener;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements ModelSimulationListener {
	private double currentAverageSpeed;
	private double averageSpeed;
	private double minSpeed;
	private double maxSpeed;
	
	public RoadSimStatistics() {
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
		// TODO Auto-generated method stub
		// log("reset: " + t);
        this.averageSpeed = 0;
		this.currentAverageSpeed = 0;
	}

	@Override
	public void notifyStepDone(final int t, final InspectorSimulation simulation) {
		this.currentAverageSpeed = 0;

        this.maxSpeed = -1;
        this.minSpeed = Double.MAX_VALUE;
		for (final var agent: simulation.agents()) {
			final CarAgent car = (CarAgent) agent;
			final double currSpeed = car.getCurrentSpeed();
            this.currentAverageSpeed += currSpeed;
			if (currSpeed > this.maxSpeed) {
                this.maxSpeed = currSpeed;
			} else if (currSpeed < this.minSpeed) {
                this.minSpeed = currSpeed;
			}
		}
		
		if (!simulation.agents().isEmpty()) {
            this.currentAverageSpeed /= simulation.agents().size();
		}
        this.log("average speed: " + this.currentAverageSpeed);
	}

	@Override
	public void notifyEnd(final InspectorSimulation simulation) {

	}

	private void log(final String msg) {
		System.out.println("[STAT] " + msg);
	}

}
