package ex1.simulation;

import ex1.car.AbstractAgent;
import ex1.inspector.road.RoadSimStatistics;
import ex1.inspector.stepper.Stepper;
import ex1.inspector.timeStatistics.TimeStatistics;
import ex1.road.AbstractEnvironment;
import ex1.synchronizers.monitor.startStop.StartStopMonitor;
import ex1.synchronizers.worker.master.MasterWorker;

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
