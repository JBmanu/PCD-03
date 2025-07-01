package model.core

import model.core.P2ds.P2d

object V2ds:

  def makeV2d(from: P2d, to: P2d): V2d = V2d(to.x - from.x, to.y - from.y)


  trait V2d():
    val x: Double
    val y: Double

    def sum(other: V2d): V2d = V2d(x + other.x, y + other.y)
    def mul(fact: Double): V2d = V2d(x * fact, y * fact)
    def abs: Double = math.sqrt(x * x + y * y)

    def normalize: V2d =
      val module = this.abs
      V2d(x / module, y / module)


  object V2d:
    def apply(x: Double, y: Double): V2d = V2dImpl(x, y)

    private case class V2dImpl(x: Double, y: Double) extends V2d
