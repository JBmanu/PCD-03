package model.core

trait Scheduler:
  val startTime: Long
  val endTime: Long
  val allTimeSpent: Long

  val currentTime: Long
  val nStepPerSec: Int
  val currentTick: Int
  val delta: Int

  def start(): Scheduler

  def stop(): Scheduler

  def averageTimeFor(stepper: Stepper): Long

  def timeElapsedSinceStart(): Long

  def nextStep(): Scheduler

  def computeDelay(): Option[Long]

object Scheduler:
  def apply(nStepPerSec: Int, delta: Int): Scheduler =
    SchedulerImpl(0L, 0L, System.currentTimeMillis(), nStepPerSec, delta, delta)

  def zero(): Scheduler = Scheduler(0, 0)

  private case class SchedulerImpl(startTime: Long, endTime: Long,
                                   currentTime: Long, nStepPerSec: Int,
                                   currentTick: Int, delta: Int) extends Scheduler:

    override val allTimeSpent: Long = endTime - startTime
    
    override def start(): Scheduler =
      val systemCurrentTime = System.currentTimeMillis()
      copy(startTime = systemCurrentTime, currentTime = systemCurrentTime)

    override def stop(): Scheduler =
      val systemCurrentTime = System.currentTimeMillis()
      copy(endTime = systemCurrentTime, currentTime = systemCurrentTime)

    override def averageTimeFor(stepper: Stepper): Long = allTimeSpent / stepper.totalStep

    override def timeElapsedSinceStart(): Long = currentTime - startTime

    override def nextStep(): Scheduler = 
      copy(currentTime = System.currentTimeMillis(), currentTick = currentTick + delta)

    override def computeDelay(): Option[Long] =
      val elapsed: Long = System.currentTimeMillis() - currentTime
      val delay: Long = (1000.0 / nStepPerSec).toLong - elapsed
      Option.when(delay > 0)(delay)
