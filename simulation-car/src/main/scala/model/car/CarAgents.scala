package model.car

import model.car.CarAgent.BaseCarAgent
import model.road.{ Environment, Road, RoadEnv }
import model.simulation.Command.Action.{ Action, MoveForward }

object CarAgents:
  private val CAR_NEAR_DIST = 15
  private val CAR_FAR_ENOUGH_DIST = 20
  private val MAX_WAITING_TIME = 2

  private enum CarAgentState:
    case STOPPED, ACCELERATING, DECELERATING_BECAUSE_OF_A_CAR, WAIT_A_BIT, MOVING_CONSTANT_SPEED


  def basic(id: String, env: RoadEnv, road: Road, initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgent =
    val carAgentBasic = CarAgentBasic(CarAgent.base(id, env, acc, dec, vmax), CarAgentState.STOPPED, 0)
    env.registerNewCar(carAgentBasic, road, initialPos)
    carAgentBasic

  def extended(id: String, env: RoadEnv, road: Road, initialPos: Double, acc: Double, dec: Double, vmax: Double): CarAgent =
    val carAgentExtended = CarAgentExtended(CarAgent.base(id, env, acc, dec, vmax), CarAgentState.STOPPED, 0)
    env.registerNewCar(carAgentExtended, road, initialPos)
    carAgentExtended

  private case class CarAgentBasic(base: BaseCarAgent,
                                   private var state: CarAgentState,
                                   private var waitingTime: Int) extends CarAgent:
    export base._

    override def decide(): Unit =
      val dt = timeDt
      state match
        case CarAgentState.STOPPED =>
        //          this.stoppedState();
        case CarAgentState.ACCELERATING =>
        //          this.acceleratingState(dt);
        case CarAgentState.MOVING_CONSTANT_SPEED =>
        //          this.movingConstantSpeedState();
        case CarAgentState.DECELERATING_BECAUSE_OF_A_CAR =>
        //          this.deceleratingBecauseOfACarState(dt);
        case CarAgentState.WAIT_A_BIT =>
      //          this.waitABitState(dt);
      if (currentSpeed > 0) selectedAction = Option(MoveForward(this.currentSpeed * dt));


  private case class CarAgentExtended(base: BaseCarAgent,
                                      private var state: CarAgentState,
                                      private var waitingTime: Int) extends CarAgent:
    export base._

    override def decide(): Unit = ???
