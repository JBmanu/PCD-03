package wrapper

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import simulation.AbstractSimulation


object SimulationActor:

  sealed trait Command
  object Start extends Command
  object NextStep extends Command
  object Pause extends Command
  object Stop extends Command
  object EndCar extends Command


  def apply(simulation: AbstractSimulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Start => Behaviors.same
          case NextStep => Behaviors.same
          case Pause => Behaviors.same
          case Stop => Behaviors.same
          case EndCar => Behaviors.same
