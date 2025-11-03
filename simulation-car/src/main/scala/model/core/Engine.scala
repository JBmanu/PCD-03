package model.core

trait Engine extends Scheduler, Stepper:
  val startTime: Long
  val endTime: Long
  val allTimeSpent: Long
  val isInPause: Boolean

  def buildSchedule(scheduler: Scheduler): Engine

  def buildStepper(stepper: Stepper): Engine

  def build(scheduler: Scheduler, stepper: Stepper): Engine

  override def start(): Engine

  def pause(): Engine

  override def stop(): Engine

  override def nextStep(): Engine

  override def setTotalStep(value: Int): Engine

  def averageTimeForStep(): Double

  def timeElapsedSinceStart(): Long

object Engine:

  def empty(): Engine = Engine(Scheduler.zero(), Stepper.zero())

  def apply(scheduler: Scheduler, stepper: Stepper): Engine = EngineImpl(scheduler, stepper, false)
  

  private case class EngineImpl(scheduler: Scheduler, stepper: Stepper, isInPause: Boolean) extends Engine:

    export scheduler.{ start => _, stop => _, nextStep => _, _ }
    export stepper.{ setTotalStep => _, nextStep => _, _ }

    override def buildSchedule(scheduler: Scheduler): Engine = copy(scheduler, isInPause = false)

    override def buildStepper(stepper: Stepper): Engine = copy(stepper = stepper, isInPause = false)

    override def build(scheduler: Scheduler, stepper: Stepper): Engine = copy(scheduler, stepper, false)

    override def start(): Engine = copy(scheduler.start(), isInPause = false)

    override def pause(): Engine = copy(isInPause = true)

    override def stop(): Engine = copy(scheduler.stop())

    override def nextStep(): Engine = copy(scheduler.nextStep(), stepper.nextStep())

    override def setTotalStep(value: Int): Engine = copy(stepper = stepper.setTotalStep(value))

    override def averageTimeForStep(): Double = scheduler.averageTimeFor(stepper)

