package model.car

import model.car.CarAgent.BaseCarAgent
import model.road.{ Environment, Road, RoadEnv }
import model.simulation.SimCommand.Action.{ Action, MoveForward }

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

  def apply(id: String, env: RoadEnv, road: Road, initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgent =
    val carAgentExtended = CarAgentExtendedImpl(CarAgent.base(id, env, acc, dec, vmax), CarAgentState.STOPPED, 0)
    env.registerNewCar(carAgentExtended, road, initialPos)
    carAgentExtended

  private case class CarAgentExtendedImpl(base: BaseCarAgent,
                                          private var state: CarAgentState,
                                          private var waitingTime: Int) extends CarAgentExtended:
    export base._

    override def decide(): Unit =
      val dt = timeDt

      state match
        case CarAgentState.STOPPED                                 => if (!detectedNearCar) state = CarAgentState.ACCELERATING
        case CarAgentState.ACCELERATING                            =>
          if (detectedNearCar) state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
          else {
            if (detectedRedOrOrgangeSemNear) state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM
            else
              currentSpeed += acceleration * dt
              if (currentSpeed >= maxSpeed) state = CarAgentState.MOVING_CONSTANT_SPEED
          }
        case CarAgentState.MOVING_CONSTANT_SPEED                   =>
          if (detectedNearCar) state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR
          else if (detectedRedOrOrgangeSemNear) state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM
        case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR           =>
          currentSpeed -= deceleration * dt
          if (currentSpeed <= 0) state = CarAgentState.STOPPED
          else if (carFarEnough)
            state = CarAgentState.WAIT_A_BIT
            waitingTime = 0
        case CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM =>
          currentSpeed -= deceleration * dt
          if (currentSpeed <= 0) state = CarAgentState.WAITING_FOR_GREEN_SEM
          else if (!detectedRedOrOrgangeSemNear) state = CarAgentState.ACCELERATING
        case CarAgentState.WAIT_A_BIT                              =>
          waitingTime += dt
          if (waitingTime > MAX_WAITING_TIME) state = CarAgentState.ACCELERATING
        case CarAgentState.WAITING_FOR_GREEN_SEM                   => if (detectedGreenSem) state = CarAgentState.ACCELERATING

      if (currentSpeed > 0) selectedAction = Option(MoveForward(currentSpeed * dt))


    private def detectedNearCar =
      val car = this.currentPercept.nearestCarInFront
      car.exists(car =>
                   val dist = car.pos - this.currentPercept.roadPos
                   dist < CAR_NEAR_DIST)

    private def detectedRedOrOrgangeSemNear: Boolean =
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
