package model.car

import model.road.{ Road, RoadEnv }
import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.CarPercept
import model.simulation.SimCommand.{ Action, Percept }

abstract case class CarAgent(private[this] val id: String,
                             private[this] val env: RoadEnv,
                             private[this] val road: Road,
                             private[this] val initialPos: Double,
                             private var _acceleration: Double,
                             private var _deceleration: Double,
                             private var _maxSpeed: Double) extends Agent(id):

  private var _currentSpeed: Double = 0D
  
  private var _timeDt: Int = 0
  private var _currentPercept: CarPercept = _
  private var _selectedAction: Option[Action] = Option.empty

//  env.registerNewCar(this, road, initialPos)

  override def timeDt: Int = _timeDt

  override def timeDt_=(dt: Int): Unit = _timeDt = dt

  override def step(dt: Int): Unit =
    // SENSE
    currentPercept = currentPercepts.asInstanceOf[CarPercept]
    // DECIDE - manca fare la parte di che il prof fa con l'ereditarietà
    selectedAction = Option.empty
    decide()
    // ACTION
    selectedAction.foreach(doAction)

  def maxSpeed: Double = _maxSpeed

  def maxSpeed_=(value: Double): Unit = _maxSpeed = value

  def currentSpeed: Double = _currentSpeed

  def currentSpeed_=(value: Double): Unit = _currentSpeed = value

  def acceleration: Double = _acceleration

  def acceleration_=(value: Double): Unit = _acceleration = value

  def deceleration: Double = _deceleration

  def deceleration_=(value: Double): Unit = _deceleration = value

  def selectedAction: Option[Action] = _selectedAction

  def selectedAction_=(action: Option[Action]): Unit = _selectedAction = action

  def currentPercept: CarPercept = _currentPercept

  def currentPercept_=(percept: CarPercept): Unit = _currentPercept = percept

  def decide(): Unit

