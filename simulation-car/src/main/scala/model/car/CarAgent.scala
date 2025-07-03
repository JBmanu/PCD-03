package model.car

import model.road.{ Environment, RoadEnv }
import model.road.unit.Road
import model.simulation.Command.{ Action, Percept }

trait CarAgent extends Agent:
  val currentSpeed: Double

  def setTimeDt(dt: Int): CarAgent

  def setCurrentSpeed(speed: Double): CarAgent

  def decide: CarAgent



object CarAgent:

  //  def apply(): CarAgent = CarAgentImpl()

  private case class CarAgentImpl(id: String, env: RoadEnv, road: Road, initialPos: Double,
                                  currentSpeed: Double, acceleration: Double, deceleration: Double, maxSpeed: Double) extends CarAgent:

    override def setCurrentSpeed(speed: Double): CarAgent = ???

    override def setTimeDt(dt: Int): CarAgent = ???

    override def init(env: Environment): Agent = ???

    override def step(dt: Int): Agent = ???

    override def currentPercepts: Percept.Percept = ???

    override def doAction(act: Action.Action): Agent = ???

    override def decide: CarAgent = ???

    
  
