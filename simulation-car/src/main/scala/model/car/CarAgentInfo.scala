package model.car

import model.road.Road

trait CarAgentInfo:
  val car: CarAgent
  val road: Road

  def pos: Double

  def pos_=(newPos: Double): Unit

object CarAgentInfo:

  def apply(car: CarAgent, road: Road, pos: Double): CarAgentInfo = CarAgentInfoImpl(car, road, pos)

  private case class CarAgentInfoImpl(car: CarAgent, road: Road,
                                      private var _pos: Double) extends CarAgentInfo:

    override def pos: Double = _pos

    override def pos_=(newPos: Double): Unit = _pos = newPos



    