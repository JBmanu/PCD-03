package ex1.simulation;

import ex1.trafficexamples.TrafficSimulationSingleRoadSeveralCars;
import ex1.trafficexamples.TrafficSimulationSingleRoadWithTrafficLightTwoCars;
import ex1.trafficexamples.TrafficSimulationWithCrossRoads;

public enum SimulationType {
    SINGLE_ROAD("Single Road", new TrafficSimulationSingleRoadSeveralCars()),
    SINGLE_ROAD_TRAFFIC_LIGHT("Single Road Traffic Light", new TrafficSimulationSingleRoadWithTrafficLightTwoCars()),
    CROSSROAD_TRAFFIC_LIGHT("Crossroad Traffic Light", new TrafficSimulationWithCrossRoads());

    private final String name;
    private final AbstractSimulation simulation;

    SimulationType(final String name, final AbstractSimulation simulation) {
        this.name = name;
        this.simulation = simulation;
    }

    public String getName() {
        return this.name;
    }
    public AbstractSimulation getSimulation() {
        return this.simulation;
    }
}