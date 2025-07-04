package model.core

object Times:

  trait Time:
    val currentSystem: Long
    val nStepPerSec: Int
    val current: Int
    val start: Int
    val delta: Int

    def setCurrentSystem: Time

    def nextStep: Time

    def computeDelay: Option[Long]


  object Time:
    def apply(nStepPerSec: Int, delta: Int): Time =
      TimeImpl(System.currentTimeMillis(), nStepPerSec, delta, delta, delta)

    private case class TimeImpl(currentSystem: Long,
                                nStepPerSec: Int,
                                current: Int, start: Int, delta: Int) extends Time:

      override def setCurrentSystem: Time = copy(currentSystem = System.currentTimeMillis())

      override def nextStep: Time = copy(current = current + delta)

      override def computeDelay: Option[Long] =
        val elapsed: Long = System.currentTimeMillis() - currentSystem
        val delay: Long = (1000.0 / nStepPerSec).toLong - elapsed
        if (delay > 0) Option(delay)
        else Option.empty


