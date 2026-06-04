package simulation;

import trafficexamples.TrafficSimulationSingleRoadSeveralCars;
import trafficexamples.TrafficSimulationSingleRoadWithTrafficLightTwoCars;
import trafficexamples.TrafficSimulationWithCrossRoads;

public enum SimulationType {
    SINGLE_ROAD("Single Road"),
    SINGLE_ROAD_TRAFFIC_LIGHT("Single Road Traffic Light"),
    CROSSROAD_TRAFFIC_LIGHT("Crossroad Traffic Light");

    private final String name;

    SimulationType(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public AbstractSimulation getSimulation() {
        return switch (this) {
            case SINGLE_ROAD -> new TrafficSimulationSingleRoadSeveralCars();
            case SINGLE_ROAD_TRAFFIC_LIGHT -> new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
            case CROSSROAD_TRAFFIC_LIGHT -> new TrafficSimulationWithCrossRoads();
        };
    }
}