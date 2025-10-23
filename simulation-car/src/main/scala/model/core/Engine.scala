package model.core

import model.core.Times.*

trait Engine extends TickScheduler, Stepper:
  val startTime: Long
  val endTime: Long
  val allTimeSpent: Long
  val isInPause: Boolean

  def build(nStepPerSec: Int, delta: Int, totalStep: Int): Engine

  def start(): Engine

  def pause(): Engine

  def stop(): Engine

  override def nextStep(): Engine

  override def setSystemCurrentTime(): Engine

  override def setTotalStep(value: Int): Engine

  def averageTimeForStep(): Double

  def timeElapsedSinceStart(): Long

object Engine:

  def apply(nStepPerSec: Int, delta: Int, totalStep: Int): Engine =
    EngineImpl(TickScheduler(nStepPerSec, delta), Stepper(totalStep), TimeStats(), false)

  def empty(): Engine = EngineImpl(TickScheduler.zero(), Stepper.zero(), TimeStats(), false)

  private case class EngineImpl(tickScheduler: TickScheduler, stepper: Stepper,
                                timeStats: TimeStats, isInPause: Boolean) extends Engine:

    export tickScheduler.{ setSystemCurrentTime => _, nextStep => _, _ }
    export stepper.{ setTotalStep => _, nextStep => _, _ }
    export timeStats.{ start => _, stop => _, averageTimeFor => _, _ }

    override def build(nStepPerSec: Int, delta: Int, totalStep: Int): Engine =
      copy(TickScheduler(nStepPerSec, delta), stepper.setTotalStep(totalStep), timeStats.start(), false)

    override def start(): Engine = copy(isInPause = false)

    override def pause(): Engine = copy(isInPause = true)

    override def stop(): Engine = copy(tickScheduler.setSystemCurrentTime(), timeStats = timeStats.stop())

    override def nextStep(): Engine = copy(tickScheduler.nextStep(), stepper.nextStep())

    override def setSystemCurrentTime(): Engine = copy(tickScheduler.setSystemCurrentTime())

    override def setTotalStep(value: Int): Engine = copy(stepper = stepper.setTotalStep(value))

    override def timeElapsedSinceStart(): Long = timeStats.timeElapsedSinceStart(tickScheduler)

    override def averageTimeForStep(): Double = timeStats.averageTimeFor(stepper)





