package model.core

import model.core.V2ds.V2d

object P2ds:

  def len(p0: P2d, p1: P2d): Double =
    val dx: Double = p0.x - p1.x
    val dy: Double = p0.y - p1.y
    Math.sqrt(dx * dx + dy * dy);

  trait P2d:
    val x: Double
    val y: Double

    def sum(other: V2d): P2d = P2d(x + other.x, y + other.y)

    def sub(other: P2d): V2d = V2d(x - other.x, y - other.y)

  object P2d:

    def apply(x: Double, y: Double): P2d = P2dImpl(x, y)

    private case class P2dImpl(x: Double, y: Double) extends P2d
