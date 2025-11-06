package wrapper

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import simulation.AbstractSimulation
import wrapper.CarActor.Init

import scala.concurrent.duration.DurationLong


object SimulationActor:

  sealed trait Command

  case class Start(totalStep: Int) extends Command

  object Stop extends Command

  object Pause extends Command

  object Resume extends Command

  private object NextStep extends Command

  object EndInitCar extends Command

  object EndStepCar extends Command


  def apply(simulation: AbstractSimulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:

          case Start(totalStep: Int) =>
            simulation.setup()
            simulation.start(totalStep)
            simulation.actors().forEach(_ ! Init(context.self, simulation))
            Behaviors.same

          case Stop => simulation.end(); Behaviors.same

          case Pause => simulation.pause(); Behaviors.same

          case Resume => simulation.resume(); context.self ! NextStep; Behaviors.same

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

          case EndInitCar =>
            simulation.increaseCounterActors()
            if simulation.allActorsDid() then
              simulation.resetCounterActors()
              simulation.notifyInit(simulation.engine().currentTick)
              context.self ! NextStep
            Behaviors.same

          case EndStepCar =>
            Behaviors.same

