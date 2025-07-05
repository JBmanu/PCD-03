package model.car

import model.car.CarAgent.BaseCarAgent
import model.road.{ Environment, Road, RoadEnv }
import model.simulation.Command.Action.Action

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
                                          private var waitingTime: Int) extends CarAgent:
    export base._

    override def decide(): Unit = ???
