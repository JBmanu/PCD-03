package model.car

import model.road.{ Environment, Road, RoadEnv }
import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.{ CarPercept, Percept }
import model.simulation.SimCommand.{ Action, Percept }

trait CarAgent extends Agent:

  def maxSpeed: Double

  def maxSpeed_=(value: Double): Unit

  def currentSpeed: Double

  def currentSpeed_=(value: Double): Unit

  def acceleration: Double

  def acceleration_=(value: Double): Unit

  def deceleration: Double

  def deceleration_=(value: Double): Unit

  def selectedAction: Option[Action]

  def selectedAction_=(action: Option[Action]): Unit

  def currentPercept: CarPercept

  def currentPercept_=(percept: CarPercept): Unit

  def decide(): Unit


object CarAgent:
  def base(id: String, env: RoadEnv, road: Road,
           initialPos: Double, acc: Double, dec: Double, vmax: Double): BaseCarAgent =
    val car = BaseCarAgent(id, env, 0, null, Option.empty, vmax, 0, acc, dec)
    env.registerNewCar(car, road, initialPos)
    car

  case class BaseCarAgent(id: String,
                          private var _env: Environment,
                          private var _timeDt: Int,
                          private var _currentPercept: CarPercept,
                          private var _selectedAction: Option[Action],

                          private var _maxSpeed: Double,
                          private var _currentSpeed: Double,
                          private var _acceleration: Double,
                          private var _deceleration: Double) extends CarAgent:


    override def env: Environment = _env

    override def env_=(newEnv: Environment): Unit = _env = newEnv

    override def timeDt: Int = _timeDt

    override def timeDt_=(dt: Int): Unit = _timeDt = dt

    override def step(dt: Int): Unit =
      // dove mettere i comandi sense -> decide -> action
      ()

    override def maxSpeed: Double = _maxSpeed

    override def maxSpeed_=(value: Double): Unit = _maxSpeed = value

    override def currentSpeed: Double = _currentSpeed

    override def currentSpeed_=(speed: Double): Unit = _currentSpeed = speed

    override def acceleration: Double = _acceleration

    override def acceleration_=(value: Double): Unit = _acceleration = value

    override def deceleration: Double = _deceleration

    override def deceleration_=(value: Double): Unit = _deceleration = value

    override def selectedAction: Option[Action] = _selectedAction

    override def selectedAction_=(action: Option[Action]): Unit = _selectedAction = action

    override def currentPercept: CarPercept = _currentPercept

    override def currentPercept_=(percept: CarPercept): Unit = _currentPercept = percept

    override def decide(): Unit = ()







