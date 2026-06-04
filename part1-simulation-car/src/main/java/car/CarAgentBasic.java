package car;

import road.MoveForward;
import road.Road;
import road.RoadsEnv;

import java.util.Optional;

/**
 * Basic Car behaviour, considering only the presence
 * of a car in front.
 * <p>
 * If there is a car and it is near, slow down.
 * If there are no cars or the car is far, accelerate up to a constant speed
 */
public class CarAgentBasic extends CarAgent {

    private static final int CAR_NEAR_DIST = 15;
    private static final int CAR_FAR_ENOUGH_DIST = 20;
    private static final int MAX_WAITING_TIME = 2;

    private enum CarAgentState {
        STOPPED, ACCELERATING,
        DECELERATING_BECAUSE_OF_A_CAR,
        WAIT_A_BIT, MOVING_CONSTANT_SPEED
    }

    private CarAgentState state;

    private int waitingTime;

    public CarAgentBasic(final String id, final RoadsEnv env, final Road road,
                         final double initialPos,
                         final double acc,
                         final double dec,
                         final double vmax) {
        super(id, env, road, initialPos, acc, dec, vmax);
        this.state = CarAgentState.STOPPED;
    }


    /**
     * Behaviour defined by a simple finite state machine
     */
    public void decide() {
        final int dt = this.timeDt();
        switch (this.state) {
            case CarAgentState.STOPPED:
                this.stoppedState();
                break;
            case CarAgentState.ACCELERATING:
                this.acceleratingState(dt);
                break;
            case CarAgentState.MOVING_CONSTANT_SPEED:
                this.movingConstantSpeedState();
                break;
            case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR:
                this.deceleratingBecauseOfACarState(dt);
                break;
            case CarAgentState.WAIT_A_BIT:
                this.waitABitState(dt);
                break;
        }

        if (this.currentSpeed > 0) {
            this.selectedAction = Optional.of(new MoveForward(this.currentSpeed * dt));
        }

    }

    // State
    private void waitABitState(final int dt) {
        this.waitingTime += dt;
        if (this.waitingTime > MAX_WAITING_TIME) {
            this.state = CarAgentState.ACCELERATING;
        }
    }

    private void deceleratingBecauseOfACarState(final int dt) {
        this.currentSpeed -= this.deceleration * dt;
        if (this.currentSpeed <= 0) {
            this.state = CarAgentState.STOPPED;
        } else if (this.carFarEnough()) {
            this.state = CarAgentState.WAIT_A_BIT;
            this.waitingTime = 0;
        }
    }

    private void movingConstantSpeedState() {
        if (this.detectedNearCar()) {
            this.state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
        }
    }

    private void acceleratingState(final int dt) {
        if (this.detectedNearCar()) {
            this.state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
        } else {
            this.currentSpeed += this.acceleration * dt;
            if (this.currentSpeed >= this.maxSpeed) {
                this.state = CarAgentState.MOVING_CONSTANT_SPEED;
            }
        }
    }

    private void stoppedState() {
        if (!this.detectedNearCar()) {
            this.state = CarAgentState.ACCELERATING;
        }
    }


    /* aux methods */
    private boolean detectedNearCar() {
        final Optional<CarAgentInfo> car = this.currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return false;
        } else {
            final double dist = car.get().getPos() - this.currentPercept.roadPos();
            return dist < CAR_NEAR_DIST;
        }
    }


    private boolean carFarEnough() {
        final Optional<CarAgentInfo> car = this.currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return true;
        } else {
            final double dist = car.get().getPos() - this.currentPercept.roadPos();
            return dist > CAR_FAR_ENOUGH_DIST;
        }
    }

}
