package model.road

import model.car.{ CarAgent, CarAgentInfo }
import model.road.unit.Road
import model.road.unit.TrafficLights.TrafficLight
import model.simulation.Command.{ Action, Percept }

trait RoadEnv extends Environment


object RoadEnv:
  val MIN_DIST_ALLOWED: Int = 5
  val CAR_DETECTION_RANGE: Int = 30
  val SEM_DETECTION_RANGE: Int = 30

  def apply(): RoadEnv = RoadEnvImpl(List.empty, List.empty, Map.empty)

  private case class RoadEnvImpl(roads: List[Road],
                                 trafficLights: List[TrafficLight],
                                 registeredCars: Map[String, CarAgentInfo]) extends RoadEnv:

    override val id: String = "traffic-env"

    override def init(): Environment = this.copy(trafficLights = trafficLights.map(_.init))

    override def step(dt: Int): Environment = this.copy(trafficLights = trafficLights.map(_ step dt))

    override def currentPercepts(agentId: String): Percept.Percept = ???

    override def doAction(agentId: String, action: Action.Action): Environment = ???


