package car;

import road.Road;

public class CarAgentInfo {

	private final CarAgent car;
	private double pos;
	private final Road road;
	
	public CarAgentInfo(final CarAgent car, final Road road, final double pos) {
		this.car = car;
		this.road = road;
		this.pos = pos;
	}
	
	public double getPos() {
		return this.pos;
	}
	
	public void updatePos(final double pos) {
		this.pos = pos;
	}
	
	public CarAgent getCar() {
		return this.car;
	}	
	
	public Road getRoad() {
		return this.road;
	}
}
