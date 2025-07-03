package model.road.unit

import model.road.unit.TrafficLights.TrafficLight

trait TrafficLightInfo:
  val sem: TrafficLight
  val road: Road
  val readPos: Double

object TrafficLightInfo:

  def apply(sem: TrafficLight, road: Road, readPos: Double): TrafficLightInfo =
    TrafficLightInfoImpl(sem, road, readPos);
  
  private case class TrafficLightInfoImpl(sem: TrafficLight, road: Road, readPos: Double) extends TrafficLightInfo 
  