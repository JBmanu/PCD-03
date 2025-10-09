package simulation.listener;

import simulation.InspectorSimulation;

public interface SimulationListener {

	/**
	 * Called at the beginning of the simulation
	 * 
	 * @param t
	 */
	void notifyInit(int t, final InspectorSimulation simulation);
	
	/**
	 * Called at each step, updater all updates
	 * @param t
	 */
	void notifyStepDone(final int t, final InspectorSimulation simulation);

	void notifyEnd(final InspectorSimulation simulation);
}
