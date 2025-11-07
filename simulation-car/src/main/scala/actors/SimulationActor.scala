package actors

import actors.CarActor.Init
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import car.CarAgent
import simulation.AbstractSimulation

import scala.concurrent.duration.DurationLong


object SimulationActor:

  sealed trait Command

  case class Start(totalStep: Int) extends Command

  object Stop extends Command

  object Pause extends Command

  object Resume extends Command

  private object NextStep extends Command

  object EndInitCar extends Command

  case class EndStepCar(carAgent: CarAgent) extends Command


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
            simulation.actors().forEach(_ ! CarActor.Step(context.self, simulation))
            Behaviors.same

          case EndInitCar =>
            simulation.increaseCounterActors()
            if simulation.allActorsDid() then
              simulation.resetCounterActors()
              simulation.actors().forEach(_ ! CarActor.Step(context.self, simulation))
            Behaviors.same

          case EndStepCar(carAgent) =>
            simulation.increaseCounterActors()

            if simulation.allActorsDid() then
              simulation.resetCounterActors()
              if !simulation.isPause then
                if simulation.engine.hasMoreSteps then
                  simulation.engine().computeDelay() match
                    case Some(value) => timers.startSingleTimer(NextStep, value.millis)
                    case None        => context.self ! NextStep
                else
                  context.self ! Stop

            Behaviors.same

