package simulation;

import car.AbstractAgent;
import inspector.RoadSimStatistics;
import inspector.Stepper;
import inspector.TimeStatistics;
import road.AbstractEnvironment;
import synchronizers.monitor.startStop.StartStopMonitor;
import synchronizers.worker.master.MasterWorker;

import java.util.List;

public interface InspectorSimulation {

    Stepper stepper();

    StartStopMonitor startStopMonitor();

    TimeStatistics timeStatistics();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<AbstractAgent> agents();

    void setMasterWorker(final MasterWorker masterWorker);

    void setup();
}
