package model.simulation

import model.car.Agent
import model.core.{ Engine, Scheduler, Stepper }
import model.inspector.{ RoadSimStatistics, TimeStatistics }
import model.road.Environment


trait Simulation extends SimulationInspector:
  def setupTimings(scheduler: Scheduler): Unit

  def setupEnvironment(env: Environment): Unit

  def addAgent(agent: Agent): Unit

  def syncWithTime(nCyclesPerSec: Int): Unit

  def addModelListener(listener: ModelSimulationListener): Unit
  // manca view

  def run(): Unit


object Simulation:
  def apply(): Simulation = SimulationImpl()

  private case class SimulationImpl() extends Simulation:

    private var _environment: Environment = _
    private var _agents: List[Agent] = List.empty

    // variables of control and manage simulation
    private var _toBeInSyncWithWallTime: Boolean = false
    private var _nStepsPerSec: Int = 0

    private var _engine: Engine = Engine.empty()

    // Tutto questo per engine
    //    private var _dt: Int = 0
    //    private var _t0: Int = 0
    // NON CI VANNO, ASPETTO A TOGLIERLI PER PULIZIA
    override val timeStatistics: TimeStatistics = TimeStatistics()
    override val stepper: Stepper = Stepper.zero()


    // elements inspector
    override val roadSimStatistics: RoadSimStatistics = RoadSimStatistics()

    // simulation listeners
    private var _modelListener: List[ModelSimulationListener] = List.empty
    // per la view


    override def environment(): Environment = _environment

    override def agents(): List[Agent] = _agents

    override def setup(): Unit = {}

    override def setupTimings(scheduler: Scheduler): Unit = _engine = _engine.buildSchedule(scheduler)

    override def setupEnvironment(env: Environment): Unit = _environment = env

    override def addAgent(agent: Agent): Unit = _agents = _agents appended agent

    override def syncWithTime(nCyclesPerSec: Int): Unit = _nStepsPerSec = nCyclesPerSec

    override def addModelListener(listener: ModelSimulationListener): Unit =
      _modelListener = _modelListener appended listener

    override def run(): Unit =
      println("GO SIMULATION")

    private def notifyReset(t0: Int): Unit =
      _modelListener.foreach(_ notifyInit(t0, this))
    // miss view listener

    private def notifyStepDone(t: Int): Unit =
      _modelListener.foreach(_ notifyStepDone(t, this))
    // miss view listener

    private def notifyEnd(): Unit =
      _modelListener.foreach(_ notifyEnd this)
    // miss view listener

    private def syncWithWallTime(): Unit = ???






