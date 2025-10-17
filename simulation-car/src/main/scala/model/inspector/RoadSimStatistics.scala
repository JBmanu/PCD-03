package model.inspector

import model.car.{ Agent, CarAgent }
import model.simulation.{ ModelSimulationListener, SimulationInspector }

trait RoadSimStatistics extends ModelSimulationListener:
  def currentAverageSpeed: Double

  def averageSpeed: Double

  def minSpeed: Double

  def maxSpeed: Double

object RoadSimStatistics:

  def apply(): RoadSimStatistics = RoadSimStatisticsImpl()

  private case class RoadSimStatisticsImpl() extends RoadSimStatistics:
    private var _currentAverageSpeed: Double = .0
    private var _averageSpeed: Double = .0
    private var _minSpeed: Double = .0
    private var _maxSpeed: Double = .0

    private def currentAverageSpeed_=(n: Double): Unit = _currentAverageSpeed = n
    private def averageSpeed_=(n: Double): Unit = _averageSpeed = n
    private def minSpeed_=(n: Double): Unit = _minSpeed = n
    private def maxSpeed_=(n: Double): Unit = _maxSpeed = n
    private def log(msg: String): Unit = println("[STAT] " + msg)

    override def currentAverageSpeed: Double = _currentAverageSpeed
    override def averageSpeed: Double = _averageSpeed
    override def minSpeed: Double = _minSpeed
    override def maxSpeed: Double = _maxSpeed


    override def notifyInit(t: Int, simulation: SimulationInspector): Unit =
      currentAverageSpeed = 0d
      averageSpeed = 0d

    override def notifyStepDone(t: Int, simulation: SimulationInspector): Unit =
      currentAverageSpeed = 0
      maxSpeed = -1d
      minSpeed = Double.MaxValue
      val agents: List[Agent] = simulation.agents()

      agents.foreach(agent => {
        val car: CarAgent = agent.asInstanceOf[CarAgent]
        val currSpeed = car.currentSpeed
        currentAverageSpeed = currentAverageSpeed + currSpeed
        if (currSpeed > maxSpeed) maxSpeed = currSpeed
        else if (currSpeed < minSpeed) minSpeed = currSpeed
      })

      if (agents.nonEmpty) currentAverageSpeed = currentAverageSpeed / agents.size
      log("average speed: " + currentAverageSpeed)

    override def notifyEnd(simulation: SimulationInspector): Unit = {}

