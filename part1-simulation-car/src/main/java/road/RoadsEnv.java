package road;

import car.CarAgent;
import car.CarAgentInfo;
import car.CarPercept;
import road.core.P2d;
import road.trafficLight.TrafficLight;
import road.trafficLight.TrafficLightInfo;
import simulation.engineseq.Action;
import simulation.engineseq.Percept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RoadsEnv extends AbstractEnvironment {
		
	private static final int MIN_DIST_ALLOWED = 5;
	private static final int CAR_DETECTION_RANGE = 30;
	private static final int SEM_DETECTION_RANGE = 30;
	
	/* list of roads */
	private final List<Road> roads;

	/* traffic lights */
	private final List<TrafficLight> trafficLights;
	
	/* cars situated in the environment */	
	private final HashMap<String, CarAgentInfo> registeredCars;


	public RoadsEnv() {
		super("traffic-env");
        this.registeredCars = new HashMap<>();
        this.trafficLights = new ArrayList<>();
        this.roads = new ArrayList<>();
	}
	
	@Override
	public void init() {
		for (final var tl: this.trafficLights) {
			tl.init();
		}
	}
	
	@Override
	public void step(final int dt) {
		for (final var tl: this.trafficLights) {
			tl.step(dt);
		}
	}
	
	public void registerNewCar(final CarAgent car, final Road road, final double pos) {
        this.registeredCars.put(car.getId(), new CarAgentInfo(car, road, pos));
	}

	public Road createRoad(final P2d p0, final P2d p1) {
		final Road r = new Road(p0, p1);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(final P2d pos, final TrafficLight.TrafficLightState initialState, final int greenDuration, final int yellowDuration, final int redDuration) {
		final TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	@Override
	public Percept getCurrentPercepts(final String agentId) {
		
		final CarAgentInfo carInfo = this.registeredCars.get(agentId);
		final double pos = carInfo.getPos();
		final Road road = carInfo.getRoad();
		final Optional<CarAgentInfo> nearestCar = this.getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
		final Optional<TrafficLightInfo> nearestSem = this.getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
		
		return new CarPercept(pos, nearestCar, nearestSem);
	}

	private Optional<CarAgentInfo> getNearestCarInFront(final Road road, final double carPos, final double range){
		return
                this.registeredCars
				.entrySet()
				.stream()
				.map(el -> el.getValue())
				.filter((carInfo) -> carInfo.getRoad() == road)
				.filter((carInfo) -> {
					final double dist = carInfo.getPos() - carPos;
					return dist > 0 && dist <= range;
				})
				.min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
	}

	private Optional<TrafficLightInfo> getNearestSemaphoreInFront(final Road road, final double carPos, final double range){
		return 
				road.getTrafficLights()
				.stream()
				.filter((TrafficLightInfo tl) -> tl.roadPos() > carPos)
				.min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
	}
	
	
	@Override
	public void doAction(final String agentId, final Action act) {
		switch (act) {
		case final MoveForward mv: {
			final CarAgentInfo info = this.registeredCars.get(agentId);
			final Road road = info.getRoad();
			final Optional<CarAgentInfo> nearestCar = this.getNearestCarInFront(road, info.getPos(), CAR_DETECTION_RANGE);
			
			if (!nearestCar.isEmpty()) {
				final double dist = nearestCar.get().getPos() - info.getPos();
				if (dist > mv.distance() + MIN_DIST_ALLOWED) {
					info.updatePos(info.getPos() + mv.distance());
				}
			} else {
				info.updatePos(info.getPos() + mv.distance());
			}

			if (info.getPos() > road.getLen()) {
				info.updatePos(0);
			}
			break;
		}
		default: break;
		}
	}
	
	
	public List<CarAgentInfo> getAgentInfo(){
		return this.registeredCars.entrySet().stream().map(el -> el.getValue()).toList();
	}

	public List<Road> getRoads(){
		return this.roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return this.trafficLights;
	}
}
