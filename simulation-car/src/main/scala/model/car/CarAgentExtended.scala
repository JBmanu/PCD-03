package model.car

import model.road.{ Road, RoadEnv }
import model.simulation.SimCommand.Action.MoveForward

trait CarAgentExtended extends CarAgent

object CarAgentExtended:
  private val CAR_NEAR_DIST = 15
  private val CAR_FAR_ENOUGH_DIST = 20
  private val MAX_WAITING_TIME = 2
  private val SEM_NEAR_DIST = 100

  private enum CarAgentState:
    case STOPPED, ACCELERATING, DECELERATING_BECAUSE_OF_A_CAR,
    DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM, WAITING_FOR_GREEN_SEM,
    WAIT_A_BIT, MOVING_CONSTANT_SPEED

  def apply(id: String, env: RoadEnv, road: Road,
            initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgentExtended =
    CarAgentExtendedImpl(id, env, road, initialPos, acc, dec, vmax)

  private class CarAgentExtendedImpl(private[this] val id: String,
                                          private[this] val env: RoadEnv, private[this] val road: Road,
                                          private[this] val initialPos: Double,
                                          private[this] val acc: Double,
                                          private[this] val dec: Double,
                                          private[this] val vmax: Double,
                                          private var _state: CarAgentState = CarAgentState.STOPPED,
                                          private var _waitingTime: Int = 0)
    extends CarAgent(id, env, road, initialPos, acc, dec, vmax) with CarAgentExtended:

    override def decide(): Unit =
      val dt = timeDt

      _state match
        case CarAgentState.STOPPED                                 => if (!detectedNearCar) _state = CarAgentState.ACCELERATING
        case CarAgentState.ACCELERATING                            =>
          if (detectedNearCar) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
          else {
            if (detectedRedOrOrangeSemNear) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM
            else
              currentSpeed += acceleration * dt
              if (currentSpeed >= maxSpeed) _state = CarAgentState.MOVING_CONSTANT_SPEED
          }
        case CarAgentState.MOVING_CONSTANT_SPEED                   =>
          if (detectedNearCar) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
          else if (detectedRedOrOrangeSemNear) _state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM
        case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR           =>
          currentSpeed -= deceleration * dt
          if (currentSpeed <= 0) _state = CarAgentState.STOPPED
          else if (carFarEnough)
            _state = CarAgentState.WAIT_A_BIT
            _waitingTime = 0
        case CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM =>
          currentSpeed -= deceleration * dt
          if (currentSpeed <= 0) _state = CarAgentState.WAITING_FOR_GREEN_SEM
          else if (!detectedRedOrOrangeSemNear) _state = CarAgentState.ACCELERATING
        case CarAgentState.WAIT_A_BIT                              =>
          _waitingTime += dt
          if (_waitingTime > MAX_WAITING_TIME) _state = CarAgentState.ACCELERATING
        case CarAgentState.WAITING_FOR_GREEN_SEM                   => if (detectedGreenSem) _state = CarAgentState.ACCELERATING

      if (currentSpeed > 0) selectedAction = Option(MoveForward(currentSpeed * dt))


    private def detectedNearCar =
      val car = this.currentPercept.nearestCarInFront
      car.exists(car =>
                   val dist = car.pos - this.currentPercept.roadPos
                   dist < CAR_NEAR_DIST)

    private def detectedRedOrOrangeSemNear: Boolean =
      val sem = this.currentPercept.nearestSem
      sem.exists(sem =>
                   val dist: Double = sem.roadPos - this.currentPercept.roadPos
                   dist > 0 && dist < SEM_NEAR_DIST)

    private def detectedGreenSem: Boolean =
      val sem = this.currentPercept.nearestSem
      sem.isDefined && sem.get.sem.isGreen

    private def carFarEnough: Boolean =
      val car = this.currentPercept.nearestCarInFront
      car.forall(car =>
                   val dist: Double = car.pos - this.currentPercept.roadPos
                   dist > CAR_FAR_ENOUGH_DIST)
