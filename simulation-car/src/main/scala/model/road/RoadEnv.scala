package model.road

import model.car.{ CarAgent, CarAgentInfo }
import model.core.P2ds.P2d
import model.road.TrafficLights.TrafficLight
import model.simulation.SimCommand.Percept.CarPercept
import model.simulation.SimCommand.{ Action, Percept }

trait RoadEnv extends Environment:
  def roads: List[Road]

  def trafficLights: List[TrafficLight]

  def registeredCars: Map[String, CarAgentInfo]

  def agentInfos: List[CarAgentInfo]

  def registerNewCar(car: CarAgent, road: Road, pos: Double): Unit

  def createRoad(p0: P2d, p1: P2d): Road

  def createTrafficLight(pos: P2d, initialState: TrafficLights.TrafficLightState, greenDuration: Int, yellowDuration: Int, redDuration: Int): TrafficLight


object RoadEnv:
  private val MIN_DIST_ALLOWED: Int = 5
  private val CAR_DETECTION_RANGE: Int = 30
  private val SEM_DETECTION_RANGE: Int = 30

  def apply(): RoadEnv = RoadEnvImpl(List.empty, List.empty, Map.empty)

  private case class RoadEnvImpl(private var _roads: List[Road],
                                 private var _trafficLights: List[TrafficLight],
                                 private var _registeredCars: Map[String, CarAgentInfo]) extends RoadEnv:

    override val id: String = "traffic-env"

    override def roads: List[Road] = _roads

    override def trafficLights: List[TrafficLight] = _trafficLights

    override def registeredCars: Map[String, CarAgentInfo] = _registeredCars

    override def agentInfos: List[CarAgentInfo] = _registeredCars.values.toList

    override def registerNewCar(car: CarAgent, road: Road, pos: Double): Unit =
      _registeredCars = _registeredCars + ((car.id, CarAgentInfo(car, road, pos)))

    override def createRoad(p0: P2d, p1: P2d): Road =
      val road = Road(p0, p1)
      _roads = _roads.::(road)
      road

    override def createTrafficLight(pos: P2d, initialState: TrafficLights.TrafficLightState, greenDuration: Int, yellowDuration: Int, redDuration: Int): TrafficLight =
      val trafficLight = TrafficLight(pos, initialState, redDuration, greenDuration, yellowDuration)
      _trafficLights = _trafficLights.::(trafficLight)
      trafficLight

    private def nearestCarInFront(road: Road, carPos: Double, range: Double): Option[CarAgentInfo] =
      _registeredCars.values.filter(carInfo => carInfo.road equals road)
                     .filter(carInfo =>
                               val dist = carInfo.pos - carPos
                               dist > 0 && dist <= range)
                     .minOption((c1, c2) => math.round((c1.pos - c2.pos).toFloat))

    private def nearestSemaphoreInFront(road: Road, carPos: Double): Option[TrafficLightInfo] =
      road.trafficLights
          .filter(tl => tl.roadPos > carPos)
          .minOption((c1, c2) => math.round((c1.roadPos - c2.roadPos).toFloat))

    override def init(): Unit = _trafficLights.foreach(_.init())

    override def step(dt: Int): Unit = _trafficLights.foreach(_ step dt)

    override def currentPercepts(agentId: String): Percept.Percept =
      val percept = _registeredCars.get(agentId).map(carAgentInfo =>
                                                       val nearestCar = nearestCarInFront(carAgentInfo.road, carAgentInfo.pos, CAR_DETECTION_RANGE)
                                                       val nearestSem = nearestSemaphoreInFront(carAgentInfo.road, carAgentInfo.pos)
                                                       CarPercept(carAgentInfo.pos, nearestCar, nearestSem))
      percept.get


    override def doAction(agentId: String, action: Action.Action): Unit =
      action match
        case Action.MoveForward(distance) =>
          val agentInfo = _registeredCars.get(agentId)
          agentInfo.foreach(info =>
                              val nearestCar = nearestCarInFront(info.road, info.pos, CAR_DETECTION_RANGE)
                              nearestCar.fold
                                        (() => info.pos = info.pos + distance)
                                        (nearestCar =>
                                           val dist = nearestCar.pos - info.pos
                                           if (dist > distance + MIN_DIST_ALLOWED) info.pos = info.pos + distance
                                         )
                              if (info.pos > info.road.len) info.pos = 0)
        case _                            => ()









