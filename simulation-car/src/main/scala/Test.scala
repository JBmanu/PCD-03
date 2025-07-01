

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem

object Test:

  object MoodActor:
    def apply(mood: String = "triste"): Behavior[String] =
      Behaviors.receive: (context, message) =>
        message match
          case "pizza" =>
            context.log.info("Che buona! Cambio umore in felice.")
            apply("felice") // cambia stato
          case "stato" =>
            context.log.info(s"Umore attuale: $mood")
            Behaviors.same
          case _       =>
            context.log.info("Messaggio non riconosciuto.")
            Behaviors.same


  def main(args: Array[String]): Unit =

    val system = ActorSystem(MoodActor(), "MoodSystem")

    system ! "stato"
    system ! "pizza"
    system ! "stato"

    Thread.sleep(1000)
    system.terminate()
