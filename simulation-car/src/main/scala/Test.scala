

import akka.actor.typed.scaladsl.{ Behaviors, TimerScheduler }
import akka.actor.typed.{ ActorSystem, Behavior }
import model.core.TimerSims.TimerSim

import scala.concurrent.duration.*

object Test:

  //  sealed trait Command
  //
  //  case object Start extends Command
  //
  //  case object Tick extends Command
  //
  //  case object DelayDone extends Command
  //
  //  case class Done(agentId: String) extends Command

  object Simulation:

    sealed trait Command

    case class Start(delay: FiniteDuration) extends Command

    private case object Tick extends Command

    def apply(timer: TimerSim): Behavior[Command] = Behaviors.withTimers { timers =>
      Behaviors.setup { context =>

        def loop(startTime: Long, delay: FiniteDuration): Behavior[Command] =
          Behaviors.receiveMessage {
            case Tick =>
              val now = System.currentTimeMillis()
              val elapsedMs = (now - startTime)
              val delayNew = 1000 - elapsedMs
              context.log.info(s"[Tick] Tempo scaduto: $elapsedMs ms. Non eseguo azione.")

              if (delayNew < 0) {
                timers.startSingleTimer(Tick, 0.millis)
              } else {
                timers.startSingleTimer(Tick, delayNew.millis)
              }

              val nextStart = System.currentTimeMillis()
              loop(nextStart, delayNew.millis)

            case _ =>
              context.log.info("Ignorato comando durante ciclo")
              Behaviors.same
          }

        Behaviors.receiveMessage {
          case Start(delay) =>
            context.log.info(s"Avvio ciclo con delay di $delay")
            val now = System.currentTimeMillis()
            timers.startSingleTimer(Tick, delay)
            loop(now, delay)
          case _         =>
            context.log.info("Comando non riconosciuto")
            Behaviors.same
        }
      }
    }

  def main(args: Array[String]): Unit =
//    val timer: TimerSim = TimerSim(1, 100)
//    val system: ActorSystem[Simulation.Command] = ActorSystem(Simulation(timer), "TimerSystem")
//    system ! Simulation.Start
    val timer: TimerSim = TimerSim(1, 100)
    val system = ActorSystem(Simulation(timer), "TimerCycle")
    system ! Simulation.Start(2.seconds)

// Ferma dopo 5 secondi per demo


//    system.scheduler.scheduleOnce(5.seconds):
//      system ! Simulation.Stop
//    }
