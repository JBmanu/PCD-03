package testAkka


import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import akka.actor.typed.scaladsl.adapter.*
import model.core.Scheduler

import scala.concurrent.Future
import scala.concurrent.duration.*


object LoopFuture:

  object Simulation:

    sealed trait Command

    case class Start(timer: Scheduler) extends Command

    case class Tick(timer: Scheduler) extends Command

    def apply(): Behavior[Command] = Behaviors.setup { context =>
      implicit val system: akka.actor.ClassicActorSystemProvider = context.system.toClassic.classicSystem

      Behaviors.receiveMessage:
        case Start(timer) =>
          context.log.info(s"Avvio ciclo con timer = $timer")
          val future = akka.pattern.after(0.millis)(Future.successful(Tick))
          context.pipeToSelf(future)(_ => Tick(timer.start()))
          Behaviors.same

        case Tick(timer) =>
          context.log.info(s"[Tick] Ricevuto Tick con timer: $timer")
          val delay = timer.computeDelay().getOrElse(0L)
          context.log.info(s"[Tick] Ora: $delay")
          // Pianifica il prossimo Tick
          val future = akka.pattern.after(delay.millis)(Future.successful(Tick))
          context.pipeToSelf(future)(_ => Tick(timer.nextStep()))
          Behaviors.same
    }

  def main(args: Array[String]): Unit =
    val timer: Scheduler = Scheduler(1, 0, 100)
    val system = ActorSystem(Simulation(), "FutureLoop")
    system ! Simulation.Start(timer)