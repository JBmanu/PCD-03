package ex1.road;

import ex1.road.core.P2d;
import ex1.road.trafficLight.TrafficLight;
import ex1.road.trafficLight.TrafficLightInfo;

import java.util.ArrayList;
import java.util.List;

public class Road {

	private final double len;
	private final P2d from;
	private final P2d to;
	private final List<TrafficLightInfo> trafficLights;

	public Road(final P2d from, final P2d to) {
		this.from = from;
		this.to = to;
		this.len = P2d.len(from, to);
        this.trafficLights = new ArrayList<>();
	}
	
	public double getLen() {
		return this.len;
	}
	
	public P2d getFrom() {
		return this.from;
	}
	
	public P2d getTo() {
		return this.to;
	}
	
	public void addTrafficLight(final TrafficLight sem, final double pos) {
        this.trafficLights.add(new TrafficLightInfo(sem, this, pos));
	}
	
	public List<TrafficLightInfo> getTrafficLights(){
		return this.trafficLights;
	}
}
