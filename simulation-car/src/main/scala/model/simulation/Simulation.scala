package model.simulation

import akka.actor.typed.ActorSystem
import model.actors.ActorCar
import model.actors.ActorCar.Init
import model.car.Agent
import model.core.{ Engine, Scheduler, Stepper }
import model.inspector.{ RoadSimStatistics, TimeStatistics }
import model.road.Environment


trait Simulation extends SimulationInspector:

  def counterCars(): Int

  def counterCars_=(value: Int): Unit

  def setup(scheduler: Scheduler, stepper: Stepper, environment: Environment, actors: List[ActorSystem[ActorCar.Command]]): Unit

  def actorCars(): List[ActorSystem[ActorCar.Command]]

  def engine(): Engine

  def start(): Simulation

  def step(): Simulation

  def pause(): Simulation

  def stop(): Simulation

  def addModelListener(listener: ModelSimulationListener): Unit
// manca view


object Simulation:
  def apply(): Simulation = SimulationImpl()

  private case class SimulationImpl() extends Simulation:

    private var _environment: Environment = _
    private var _cars: List[ActorSystem[ActorCar.Command]] = List.empty
    private var _counterCars = 0
    //    private var _agents: List[Agent] = List.empty

    // Tutto questo per engine
    private var _engine: Engine = Engine.empty()

    // NON CI VANNO, ASPETTO A TOGLIERLI PER PULIZIA
    private var _timeStatistics: TimeStatistics = TimeStatistics()
    private var _roadSimStatistics: RoadSimStatistics = RoadSimStatistics()

    // simulation listeners
    private var _modelListener: List[ModelSimulationListener] = List.empty
    // manca per la view


    override def counterCars(): Int = _counterCars

    override def counterCars_=(value: Int): Unit = _counterCars = value

    override def stepper(): Stepper = _engine.stepper

    override def timeStatistics(): TimeStatistics = _timeStatistics

    override def roadSimStatistics(): RoadSimStatistics = _roadSimStatistics

    override def environment(): Environment = _environment

    override def agents(): List[Agent] = ??? // _cars.map(_.)

    override def setup(scheduler: Scheduler, stepper: Stepper, environment: Environment, actors: List[ActorSystem[ActorCar.Command]]): Unit =
      _engine = _engine.build(scheduler, stepper)
      _environment = environment
      _cars = actors

    override def actorCars(): List[ActorSystem[ActorCar.Command]] = _cars

    override def engine(): Engine = _engine

    override def start(): Simulation =
      _engine = _engine.start()
      environment().init()
      actorCars().foreach(_ ! Init(environment()))
      notifyReset(_engine.currentTick)
      this

    override def step(): Simulation =
      _engine = _engine.nextStep()
      _counterCars = 0
      environment().step(_engine.currentTick)
      notifyStepDone(_engine.currentTick)
      this

    override def pause(): Simulation =
      _engine = _engine.pause()
      this

    override def stop(): Simulation =
      _engine = _engine.stop()
      notifyEnd()
      this

    override def addModelListener(listener: ModelSimulationListener): Unit =
      _modelListener = _modelListener appended listener


    private def notifyReset(t0: Int): Unit =
      _modelListener.foreach(_ notifyInit(t0, this))
    // miss view listener

    private def notifyStepDone(t: Int): Unit =
      _modelListener.foreach(_ notifyStepDone(t, this))
    // miss view listener

    private def notifyEnd(): Unit =
      _modelListener.foreach(_ notifyEnd this)





// miss view listener






