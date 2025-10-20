package model.core

import model.core.Times.*

trait Engine extends TickScheduler, Stepper:
  val startTime: Long
  val endTime: Long
  val allTimeSpent: Long

  def start(nStepPerSec: Int, delta: Int, totalStep: Int): Engine

  def stop(): Engine

  override def nextStep(): Engine

  override def setSystemCurrentTime(): Engine

  override def setTotalStep(value: Int): Engine

  def averageTimeForStep(): Double

  def timeElapsedSinceStart(): Long

object Engine:

  def apply(): Engine = EngineImpl(TickScheduler.zero(), Stepper.zero(), TimeStats())

  private case class EngineImpl(tickScheduler: TickScheduler, stepper: Stepper, timeStats: TimeStats) extends Engine:
    export tickScheduler.{ setSystemCurrentTime => _, nextStep => _, _ }
    export stepper.{ setTotalStep => _, nextStep => _, _ }
    export timeStats.{ start => _, stop => _, averageTimeFor => _, _ }

    override def start(nStepPerSec: Int, delta: Int, totalStep: Int): Engine =
      val newTickScheduler = TickScheduler(nStepPerSec, delta)
      copy(newTickScheduler, stepper.setTotalStep(totalStep), timeStats.start(newTickScheduler))

    override def stop(): Engine =
      val newTickScheduler = tickScheduler.setSystemCurrentTime()
      copy(newTickScheduler, timeStats = timeStats.stop(newTickScheduler, stepper))

    override def nextStep(): Engine = copy(tickScheduler.nextStep(), stepper.nextStep())

    override def setSystemCurrentTime(): Engine = copy(tickScheduler.setSystemCurrentTime())

    override def setTotalStep(value: Int): Engine = copy(stepper = stepper.setTotalStep(value))

    override def timeElapsedSinceStart(): Long = timeStats.timeElapsedSinceStart(tickScheduler)

    override def averageTimeForStep(): Double = timeStats.averageTimeFor(stepper)





