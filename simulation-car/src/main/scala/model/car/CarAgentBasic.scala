package model.car

import model.road.{ Road, RoadEnv }
import model.simulation.SimCommand.Action.MoveForward

trait CarAgentBasic extends CarAgent

object CarAgentBasic:
  private val CAR_NEAR_DIST = 15
  private val CAR_FAR_ENOUGH_DIST = 20
  private val MAX_WAITING_TIME = 2

  private enum CarAgentState:
    case STOPPED, ACCELERATING, DECELERATING_BECAUSE_OF_A_CAR, WAIT_A_BIT, MOVING_CONSTANT_SPEED

  def apply(id: String, env: RoadEnv, road: Road,
            initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgentBasic =
    CarAgentBasicImpl(id, env, road, initialPos, acc, dec, vmax)

  private class CarAgentBasicImpl(private[this] val id: String,
                                  private[this] val env: RoadEnv, private[this] val road: Road,
                                  private[this] val initialPos: Double,
                                  private[this] val acc: Double, private[this] val dec: Double,
                                  private[this] val vmax: Double,
                                  private var _state: CarAgentState = CarAgentState.STOPPED,
                                  private var _waitingTime: Int = 0)
    extends CarAgent(id, env, road, initialPos, acc, dec, vmax) with CarAgentBasic:

    override def decide(): Unit =
      val dt = timeDt
      _state match
        case CarAgentState.STOPPED                       => stoppedState();
        case CarAgentState.ACCELERATING                  => acceleratingState(dt);
        case CarAgentState.MOVING_CONSTANT_SPEED         => movingConstantSpeedState();
        case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR => deceleratingBecauseOfACarState(dt);
        case CarAgentState.WAIT_A_BIT                    => waitABitState(dt);
      if (currentSpeed > 0) selectedAction = Option(MoveForward(currentSpeed * dt));

    // State
    private def waitABitState(dt: Int): Unit =
      _waitingTime += dt
      if (this._waitingTime > MAX_WAITING_TIME) _state = CarAgentState.ACCELERATING

    private def deceleratingBecauseOfACarState(dt: Int): Unit =
      currentSpeed -= (deceleration * dt)
      if (currentSpeed <= 0) _state = CarAgentState.STOPPED
      else if (carFarEnough)
        _state = CarAgentState.WAIT_A_BIT
        _waitingTime = 0

    private def movingConstantSpeedState(): Unit =
      if (detectedNearCar) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR

    private def acceleratingState(dt: Int): Unit =
      if (detectedNearCar) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
      else
        currentSpeed += acceleration * dt
        if (currentSpeed >= maxSpeed) _state = CarAgentState.MOVING_CONSTANT_SPEED

    private def stoppedState(): Unit =
      if (!this.detectedNearCar) _state = CarAgentState.ACCELERATING


    /* aux methods */
    private def detectedNearCar =
      val car = currentPercept.nearestCarInFront
      car.exists(car =>
                   val dist = car.pos - currentPercept.roadPos
                   dist < CAR_NEAR_DIST)

    private def carFarEnough =
      val car = currentPercept.nearestCarInFront
      car.forall(car =>
                   val dist = car.pos - currentPercept.roadPos
                   dist > CAR_FAR_ENOUGH_DIST)
