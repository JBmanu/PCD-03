package simulation;

import car.AbstractAgent;
import inspector.road.RoadSimStatistics;
import inspector.stepper.Stepper;
import inspector.timeStatistics.TimeStatistics;
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
