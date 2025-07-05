package model.simulation

import model.car.CarAgentInfo
import model.road.TrafficLightInfo

object Command:

  object Action:
    trait Action

    case class MoveForward(distance: Double) extends Action


  object Percept:
    trait Percept

    case class CarPercept(roadPos: Double, nearestCarInFront: Option[CarAgentInfo], nearestSem: Option[TrafficLightInfo]) extends Percept
    
    