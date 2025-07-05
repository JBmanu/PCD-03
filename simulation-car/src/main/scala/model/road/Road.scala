package model.road

import model.core.P2ds
import model.core.P2ds.P2d
import model.road.TrafficLights.TrafficLight

trait Road:
  val len: Double
  val from: P2d
  val to: P2d

  def trafficLights: List[TrafficLightInfo]

  def addTrafficLight(sem: TrafficLight, pos: Double): Unit

object Road:

  def apply(from: P2d, to: P2d): Road = RoadImpl(P2ds.len(from, to), from, to, List.empty)

  private case class RoadImpl(len: Double, from: P2d, to: P2d, private var _trafficLights: List[TrafficLightInfo]) extends Road:

    override def trafficLights: List[TrafficLightInfo] = _trafficLights
    
    override def addTrafficLight(sem: TrafficLight, pos: Double): Unit =
      _trafficLights = _trafficLights.::(TrafficLightInfo(sem, this, pos))


