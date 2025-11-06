package wrapper

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import simulation.AbstractSimulation

import scala.concurrent.duration.DurationLong


object SimulationActor:

  sealed trait Command

  case class Start(totalStep: Int) extends Command

  private object NextStep extends Command

  object Pause extends Command

  object Resume extends Command

  object Stop extends Command

  object EndCar extends Command


  def apply(simulation: AbstractSimulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Start(totalStep: Int) =>
            simulation.setTotalSteps(totalStep)
            simulation.setup()
            simulation.start()
            context.self ! NextStep
            Behaviors.same

          case NextStep =>
            simulation.nextStep()
            if !simulation.isPause then
              if simulation.engine.hasMoreSteps then
                simulation.engine().computeDelay() match
                  case Some(value) => timers.startSingleTimer(NextStep, value.millis)
                  case None        => context.self ! NextStep
              else
                context.self ! Stop
            Behaviors.same

          case Pause =>
            simulation.pause()
            Behaviors.same

          case Resume =>
            simulation.resume();
            context.self ! NextStep
            Behaviors.same

          case Stop =>
            simulation.end()
            Behaviors.same

          case EndCar =>
            Behaviors.same

