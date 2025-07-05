package model.road

import TrafficLights.TrafficLight

trait TrafficLightInfo:
  val sem: TrafficLight
  val road: Road
  val roadPos: Double

object TrafficLightInfo:

  def apply(sem: TrafficLight, road: Road, readPos: Double): TrafficLightInfo =
    TrafficLightInfoImpl(sem, road, readPos);
  
  private case class TrafficLightInfoImpl(sem: TrafficLight, road: Road, roadPos: Double) extends TrafficLightInfo 
  