package simulation;

import car.AbstractAgent;
import inspector.RoadSimStatistics;
import inspector.Stepper;
import inspector.TimeStatistics;
import road.AbstractEnvironment;

import java.util.List;

public interface InspectorSimulation {

    Stepper stepper();

    TimeStatistics timeStatistics();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<AbstractAgent> agents();

    void setup();
}
