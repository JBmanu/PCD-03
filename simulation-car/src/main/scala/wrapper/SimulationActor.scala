package wrapper

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorSystem, Behavior }
import simulation.AbstractSimulation


object SimulationActor:

  sealed trait Command

  case class Setup(totalStep: Int) extends Command

  object Start extends Command

  object NextStep extends Command

  object Pause extends Command

  object Resume extends Command

  object Stop extends Command

  object EndCar extends Command


  def apply(simulation: AbstractSimulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Setup(totalStep: Int) =>
            simulation.stepper().setTotalStep(totalStep)
            Behaviors.same

          case Start =>
            simulation.init()
            context.self ! NextStep
            Behaviors.same

          case NextStep =>
            simulation.nextStep()
            if (!simulation.isPause) context.self ! NextStep
            Behaviors.same

          case Pause =>
            simulation.pause()
            Behaviors.same

          case Resume =>
            simulation.play()
            context.self ! NextStep
            Behaviors.same

          case Stop =>
            simulation.end()
            Behaviors.same

          case EndCar =>
            Behaviors.same

