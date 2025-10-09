package road.trafficLight;

import road.core.P2d;

/**
 * Class modeling the structure and behaviour of a traffic light
 *  
 */
public class TrafficLight {
	
	public enum TrafficLightState {GREEN, YELLOW, RED}
	private TrafficLightState state;
    private final TrafficLightState initialState;
	private int currentTimeInState;
	private final int redDuration;
    private final int greenDuration;
    private final int yellowDuration;
	private final P2d pos;
	
	public TrafficLight(final P2d pos, final TrafficLightState initialState, final int greenDuration, final int yellowDuration, final int redDuration) {
		this.redDuration = redDuration;
		this.greenDuration = greenDuration;
		this.yellowDuration = yellowDuration;
		this.pos = pos;
		this.initialState = initialState;
	}
	
	public void init() {
        this.state = this.initialState;
        this.currentTimeInState = 0;
	}

	public void step(final int dt) {
		switch (this.state) {
		case TrafficLightState.GREEN:
            this.currentTimeInState += dt;
			if (this.currentTimeInState >= this.greenDuration) {
                this.state = TrafficLightState.YELLOW;
                this.currentTimeInState = 0;
			}
			break;
		case TrafficLightState.RED:
            this.currentTimeInState += dt;
			if (this.currentTimeInState >= this.redDuration) {
                this.state = TrafficLightState.GREEN;
                this.currentTimeInState = 0;
			}
			break;
		case TrafficLightState.YELLOW:
            this.currentTimeInState += dt;
			if (this.currentTimeInState >= this.yellowDuration) {
                this.state = TrafficLightState.RED;
                this.currentTimeInState = 0;
			}
			break;
		default:
			break;
		}
	}
	
	public boolean isGreen() {
		return this.state.equals(TrafficLightState.GREEN);
	}
	
	public boolean isRed() {
		return this.state.equals(TrafficLightState.RED);
	}

	public boolean isYellow() {
		return this.state.equals(TrafficLightState.YELLOW);
	}
	
	public P2d getPos() {
		return this.pos;
	}
}
