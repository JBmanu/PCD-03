package trafficexamples;

import car.CarAgent;
import car.CarAgentExtended;
import road.Road;
import road.RoadsEnv;
import road.core.P2d;
import road.trafficLight.TrafficLight;
import simulation.AbstractSimulation;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars() {
		super();
	}
	
	public void setup() {

		this.setupTimings(0, 1);
		
		final RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
				
		final Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));

		final TrafficLight tl = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
		r.addTrafficLight(tl, 740);
		
		final CarAgent car1 = new CarAgentExtended("car-1", env, r, 0, 0.1, 0.3, 6);
		this.addAgent(car1);		
		final CarAgent car2 = new CarAgentExtended("car-2", env, r, 100, 0.1, 0.3, 5);
		this.addAgent(car2);

		this.syncWithTime(25);
	}	
	
}
