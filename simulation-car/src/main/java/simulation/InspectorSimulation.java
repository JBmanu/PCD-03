package simulation;

import car.AbstractAgent;
import inspector.RoadSimStatistics;
import inspector.TimeStatistics;
import model.core.Engine;
import road.AbstractEnvironment;

import java.util.List;

public interface InspectorSimulation {

    Engine engine();

    TimeStatistics timeStatistics();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<AbstractAgent> agents();

    void setup();
}
