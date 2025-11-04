package model.simulation

import model.car.Agent
import model.core.{ Engine, Scheduler, Stepper }
import model.inspector.{ RoadSimStatistics, TimeStatistics }
import model.road.Environment


trait Simulation extends SimulationInspector:
  def setup(scheduler: Scheduler, stepper: Stepper, environment: Environment, agents: List[Agent]): Unit

  def start(): Simulation

  def step(): Simulation

  def pause(): Simulation

  def stop(): Simulation

  def addModelListener(listener: ModelSimulationListener): Unit
  // manca view

  def run(): Unit


object Simulation:
  def apply(): Simulation = SimulationImpl()

  private case class SimulationImpl() extends Simulation:

    private var _environment: Environment = _
    private var _agents: List[Agent] = List.empty

    // Tutto questo per engine
    private var _engine: Engine = Engine.empty()

    // NON CI VANNO, ASPETTO A TOGLIERLI PER PULIZIA
    private var _timeStatistics: TimeStatistics = TimeStatistics()
    private var _roadSimStatistics: RoadSimStatistics = RoadSimStatistics()

    // simulation listeners
    private var _modelListener: List[ModelSimulationListener] = List.empty
    // manca per la view

    override def stepper(): Stepper = _engine.stepper

    override def timeStatistics(): TimeStatistics = _timeStatistics

    override def roadSimStatistics(): RoadSimStatistics = _roadSimStatistics

    override def environment(): Environment = _environment

    override def agents(): List[Agent] = _agents

    override def setup(): Unit = {}

    override def setup(scheduler: Scheduler, stepper: Stepper, environment: Environment, agents: List[Agent]): Unit =
      _engine = _engine.build(scheduler, stepper)
      _environment = environment
      _agents = agents

    override def start(): Simulation =
      _engine = _engine.start()
      this

    override def step(): Simulation =
      _engine = _engine.nextStep()
      this

    override def pause(): Simulation =
      _engine = _engine.pause()
      this

    override def stop(): Simulation =
      _engine = _engine.stop()
      this

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






