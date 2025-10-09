package ex1.trafficexamples;

import ex1.car.CarAgent;
import ex1.car.CarAgentBasic;
import ex1.road.Road;
import ex1.road.RoadsEnv;
import ex1.road.core.P2d;
import ex1.simulation.AbstractSimulation;

/**
 * 
 * Traffic Simulation about a number of cars 
 * moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadSeveralCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadSeveralCars() {
		super();
	}
	
	public void setup() {

		this.setupTimings(0, 1);

		final RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		
		final Road road = env.createRoad(new P2d(0,300), new P2d(1500,300));

		final int nCars = 30;

		for (int i = 0; i < nCars; i++) {
			
			final String carId = "car-" + i;
			// double initialPos = i*30;
			final double initialPos = i*10;
			
			final double carAcceleration = 1; //  + gen.nextDouble()/2;
			final double carDeceleration = 0.3; //  + gen.nextDouble()/2;
			final double carMaxSpeed = 7; // 4 + gen.nextDouble();
						
			final CarAgent car = new CarAgentBasic(carId, env,
									road,
									initialPos, 
									carAcceleration, 
									carDeceleration,
									carMaxSpeed);
			this.addAgent(car);
		}
		
		this.syncWithTime(25);
	}	
}
	