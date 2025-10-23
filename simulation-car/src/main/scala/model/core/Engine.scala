package model.core

import model.core.Scheduler

trait Engine extends Scheduler, Stepper:
  val startTime: Long
  val endTime: Long
  val allTimeSpent: Long
  val isInPause: Boolean

  def build(nStepPerSec: Int, delta: Int, totalStep: Int): Engine

  override def start(): Engine

  def pause(): Engine

  override def stop(): Engine

  override def nextStep(): Engine

  override def setTotalStep(value: Int): Engine

  def averageTimeForStep(): Double

  def timeElapsedSinceStart(): Long

object Engine:

  def apply(nStepPerSec: Int, delta: Int, totalStep: Int): Engine =
    EngineImpl(Scheduler(nStepPerSec, delta), Stepper(totalStep), false)

  def empty(): Engine = EngineImpl(Scheduler.zero(), Stepper.zero(), false)

  private case class EngineImpl(scheduler: Scheduler, stepper: Stepper, isInPause: Boolean) extends Engine:

    export scheduler.{ start => _, stop => _, setSystemCurrentTime => _, nextStep => _, _ }
    export stepper.{ setTotalStep => _, nextStep => _, _ }

    override def build(nStepPerSec: Int, delta: Int, totalStep: Int): Engine =
      copy(Scheduler(nStepPerSec, delta), stepper.setTotalStep(totalStep), false)

    override def start(): Engine = copy(scheduler.start(), isInPause = false)

    override def pause(): Engine = copy(isInPause = true)

    override def stop(): Engine = copy(scheduler.stop())

    override def nextStep(): Engine = copy(scheduler.nextStep(), stepper.nextStep())

    override def setTotalStep(value: Int): Engine = copy(stepper = stepper.setTotalStep(value))

    override def averageTimeForStep(): Double = scheduler.averageTimeFor(stepper)





