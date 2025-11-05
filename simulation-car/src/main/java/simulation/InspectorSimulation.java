package simulation;

import car.AbstractAgent;
import inspector.RoadSimStatistics;
import wrapper.Engine;
import road.AbstractEnvironment;

import java.util.List;

public interface InspectorSimulation {

    Engine engine();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<AbstractAgent> agents();

    void setup();
}
