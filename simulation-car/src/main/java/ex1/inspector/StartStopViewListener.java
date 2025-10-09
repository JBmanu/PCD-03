package ex1.inspector;

import ex1.simulation.InspectorSimulation;

public interface StartStopViewListener {

    boolean conditionToStart(final InspectorSimulation simulation);

    void onStart(final InspectorSimulation simulation);

    void reset(final InspectorSimulation simulation);
}
