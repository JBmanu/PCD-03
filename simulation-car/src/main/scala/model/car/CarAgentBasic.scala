package model.car

import model.car.CarAgent.BaseCarAgent
import model.road.{ Road, RoadEnv }
import model.simulation.Command.Action.MoveForward

trait CarAgentBasic extends CarAgent

object CarAgentBasic:
  private val CAR_NEAR_DIST = 15
  private val CAR_FAR_ENOUGH_DIST = 20
  private val MAX_WAITING_TIME = 2

  private enum CarAgentState:
    case STOPPED, ACCELERATING, DECELERATING_BECAUSE_OF_A_CAR, WAIT_A_BIT, MOVING_CONSTANT_SPEED

  def apply(id: String, env: RoadEnv, road: Road, initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgent =
    val carAgentBasic = CarAgentBasicImpl(CarAgent.base(id, env, acc, dec, vmax), CarAgentState.STOPPED, 0)
    env.registerNewCar(carAgentBasic, road, initialPos)
    carAgentBasic

  private case class CarAgentBasicImpl(base: BaseCarAgent,
                                       private var state: CarAgentState,
                                       private var waitingTime: Int) extends CarAgentBasic:
    export base._

    override def decide(): Unit =
      val dt = timeDt
      state match
        case CarAgentState.STOPPED                       => stoppedState();
        case CarAgentState.ACCELERATING                  => acceleratingState(dt);
        case CarAgentState.MOVING_CONSTANT_SPEED         => movingConstantSpeedState();
        case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR => deceleratingBecauseOfACarState(dt);
        case CarAgentState.WAIT_A_BIT                    => waitABitState(dt);
      if (currentSpeed > 0) selectedAction = Option(MoveForward(currentSpeed * dt));

    // State
    private def waitABitState(dt: Int): Unit =
      waitingTime += dt
      if (this.waitingTime > MAX_WAITING_TIME) state = CarAgentState.ACCELERATING

    private def deceleratingBecauseOfACarState(dt: Int): Unit =
      currentSpeed -= (deceleration * dt)
      if (currentSpeed <= 0) state = CarAgentState.STOPPED
      else if (carFarEnough)
        state = CarAgentState.WAIT_A_BIT
        waitingTime = 0

    private def movingConstantSpeedState(): Unit =
      if (detectedNearCar) state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR

    private def acceleratingState(dt: Int): Unit =
      if (detectedNearCar) state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
      else
        currentSpeed += acceleration * dt
        if (currentSpeed >= maxSpeed) state = CarAgentState.MOVING_CONSTANT_SPEED

    private def stoppedState(): Unit =
      if (!this.detectedNearCar) state = CarAgentState.ACCELERATING


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
