package model.core

object TimerSims:

  trait TimerSim:
    val currentSystem: Long
    val nStepPerSec: Int
    val current: Int
    val start: Int
    val delta: Int

    def setCurrentSystem: TimerSim

    def nextStep: TimerSim

    def computeDelay: Option[Long]


  object TimerSim:
    def apply(nStepPerSec: Int, delta: Int): TimerSim =
      TimerSimImpl(System.currentTimeMillis(), nStepPerSec, delta, delta, delta)

    private case class TimerSimImpl(currentSystem: Long,
                                    nStepPerSec: Int,
                                    current: Int, start: Int, delta: Int) extends TimerSim:

      override def setCurrentSystem: TimerSim = copy(currentSystem = System.currentTimeMillis())

      override def nextStep: TimerSim = copy(current = current + delta)

      override def computeDelay: Option[Long] =
        val elapsed: Long = System.currentTimeMillis() - currentSystem
        println(s"elapsed: $elapsed")
        val delay: Long = (1000.0 / nStepPerSec).toLong - elapsed
        println(s"delay: $delay")
        if (delay > 0) Option(delay)
        else Option.empty



