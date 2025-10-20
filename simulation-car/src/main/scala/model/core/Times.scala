package model.core

object Times:

  trait TickScheduler:
    val currentTime: Long
    val nStepPerSec: Int
    val currentTick: Int
    val delta: Int

    def setSystemCurrentTime(): TickScheduler

    def nextStep(): TickScheduler

    def computeDelay: Option[Long]

  object TickScheduler:
    def apply(nStepPerSec: Int, delta: Int): TickScheduler =
      TickSchedulerImpl(System.currentTimeMillis(), nStepPerSec, delta, delta)
      
    def zero(): TickScheduler = TickScheduler(0, 0)

    private case class TickSchedulerImpl(currentTime: Long, nStepPerSec: Int,
                                         currentTick: Int, delta: Int) extends TickScheduler:

      override def setSystemCurrentTime(): TickScheduler = copy(currentTime = System.currentTimeMillis())

      override def nextStep(): TickScheduler = copy(currentTick = currentTick + delta)

      override def computeDelay: Option[Long] =
        val elapsed: Long = System.currentTimeMillis() - currentTime
        val delay: Long = (1000.0 / nStepPerSec).toLong - elapsed
        Option.when(delay > 0)(delay)

  trait TimeStats:
    val startTime: Long
    val endTime: Long
    val allTimeSpent: Long

    def averageTimeFor(stepper: Stepper): Long

    def start(tickScheduler: TickScheduler): TimeStats

    def stop(tickScheduler: TickScheduler, stepper: Stepper): TimeStats

    def timeElapsedSinceStart(tickScheduler: TickScheduler): Long

  object TimeStats:
    def apply(): TimeStats = TimeStatsImpl(0L, 0L)

    private case class TimeStatsImpl(startTime: Long, endTime: Long) extends TimeStats:

      override val allTimeSpent: Long = endTime - startTime

      override def averageTimeFor(stepper: Stepper): Long = allTimeSpent / stepper.totalStep

      override def start(scheduler: TickScheduler): TimeStats = copy(startTime = scheduler.currentTime)

      override def stop(scheduler: TickScheduler, stepper: Stepper): TimeStats = copy(endTime = scheduler.currentTime)

      override def timeElapsedSinceStart(scheduler: TickScheduler): Long = scheduler.currentTime - startTime

