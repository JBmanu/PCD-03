package model.core

import model.inspector.Stepper

object Times:

  trait TickScheduler:
    val currentTime: Long
    val nStepPerSec: Int
    val currentTick: Int
    val delta: Int

    def setSystemCurrentTime(): TickScheduler

    def nextStep: TickScheduler

    def computeDelay: Option[Long]

  object TickScheduler:
    def apply(nStepPerSec: Int, delta: Int): TickScheduler =
      TickSchedulerImpl(System.currentTimeMillis(), nStepPerSec, delta, delta)

    private case class TickSchedulerImpl(currentTime: Long, nStepPerSec: Int,
                                         currentTick: Int, delta: Int) extends TickScheduler:

      override def setSystemCurrentTime(): TickScheduler = copy(currentTime = System.currentTimeMillis())

      override def nextStep: TickScheduler = copy(currentTick = currentTick + delta)

      override def computeDelay: Option[Long] =
        val elapsed: Long = System.currentTimeMillis() - currentTime
        val delay: Long = (1000.0 / nStepPerSec).toLong - elapsed
        Option.when(delay > 0)(delay)

  trait TimeStats:

    def allTimeSpent: Long

    def averageTimeForStep: Double

    def updateStartTimeWithSystem(): Unit

    def updateEndTimeWithSystem(): Unit

    def averageTimeForStep_=(stepper: Stepper): Unit

    def timeElapsedSinceStart(tickScheduler: TickScheduler): Long


  object TimeStats:
    def apply(): TimeStats = TimeStatsImpl()

    private case class TimeStatsImpl() extends TimeStats:
      private var _startTime = 0L
      private var _endTime = 0L
      private var _averageTimeForStep = 0d

      override def allTimeSpent: Long = _endTime - _startTime

      override def averageTimeForStep: Double = _averageTimeForStep

      override def updateStartTimeWithSystem(): Unit = _startTime = System.currentTimeMillis()

      override def updateEndTimeWithSystem(): Unit = _endTime = System.currentTimeMillis()

      override def averageTimeForStep_=(stepper: Stepper): Unit = _averageTimeForStep = allTimeSpent / stepper.totalStep

      override def timeElapsedSinceStart(tickScheduler: TickScheduler): Long = tickScheduler.currentTime - _startTime


