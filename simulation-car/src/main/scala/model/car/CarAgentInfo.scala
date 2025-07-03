package model.car

import model.road.unit.Road

trait CarAgentInfo:
  val car: CarAgent
  val pos: Double
  val road: Road

  def updatePos(newPos: Double): CarAgentInfo

object CarAgentInfo:

  def apply(car: CarAgent, pos: Double, road: Road): CarAgentInfo = CarAgentInfoImpl(car, pos, road)

  private case class CarAgentInfoImpl(car: CarAgent, pos: Double, road: Road) extends CarAgentInfo:
    override def updatePos(newPos: Double): CarAgentInfo = copy(pos = newPos)



    