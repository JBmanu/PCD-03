package model.car

import model.road.{ Environment, Road, RoadEnv }
import model.simulation.Command.Action.Action
import model.simulation.Command.Percept.CarPercept
import model.simulation.Command.{ Action, Percept }

trait CarAgent extends Agent:

  def selectedAction: Option[Action]

  def selectedAction_=(action: Option[Action]): Unit

  def currentSpeed: Double

  def currentPercept_=(percept: CarPercept): Unit

  def decide(): Unit


object CarAgent:
  def base(id: String, env: RoadEnv, acc: Double, dec: Double, vmax: Double): BaseCarAgent =
    BaseCarAgent(id, env, 0, null, Option.empty, vmax, 0, acc, dec)


  case class BaseCarAgent(id: String,
                          private var _env: Environment,
                          private var _timerDt: Int,
                          private var _currentPercept: CarPercept,
                          private var _selectedAction: Option[Action],
                          private var _maxSpeed: Double,
                          private var _currentSpeed: Double,
                          private var _acceleration: Double,
                          private var _deceleration: Double):
    def env: Environment = _env

    def timeDt: Int = _timerDt

    def timeDt_=(dt: Int): Unit = _timerDt = dt

    def currentPercepts: CarPercept = _currentPercept

    def init(env: Environment): Unit = _env = env

    def step(dt: Int): Unit = ()

    def doAction(act: Action): Unit = env.doAction(id, act)


    def selectedAction: Option[Action] = _selectedAction

    def selectedAction_=(action: Option[Action]): Unit = _selectedAction = action

    def currentSpeed: Double = _currentSpeed

    def currentPercept_=(percept: CarPercept): Unit = _currentPercept = percept




