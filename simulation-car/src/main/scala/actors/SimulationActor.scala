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

  case class EndStepSenseDecideCar(carAgent: CarAgent) extends Command

  case class EndStepActCar(carAgent: CarAgent) extends Command

  def apply(simulation: AbstractSimulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        var started = false

        Behaviors.receiveMessage:

          case Start(totalStep: Int) =>
            if !started then
              started = true
              simulation.setup()
              simulation.start(totalStep)
              // created for each agent an actor
              simulation.agents().forEach: agent =>
                val actorRef = context.spawn(CarActor.apply(agent), agent.getId())
                simulation.addActor(actorRef)
              simulation.actors().forEach(_ ! Init(context.self, simulation))
            Behaviors.same

          case Stop => simulation.end(); Behaviors.stopped

          case Pause => simulation.pause(); Behaviors.same

          case Resume => simulation.resume(); context.self ! NextStep; Behaviors.same

          case NextStep =>
            simulation.nextStep()
            simulation.actors().forEach(_ ! CarActor.StepSenseDecide(context.self, simulation))
            Behaviors.same

          case EndInitCar =>
            simulation.increaseCounterActors()
            if simulation.allActorsDid() then
              simulation.resetCounterActors()
              simulation.actors().forEach(_ ! CarActor.StepSenseDecide(context.self, simulation))
            Behaviors.same

          case EndStepSenseDecideCar(carAgent) =>
            simulation.increaseCounterActors()
            simulation.roadStatistics().addCarSpeed(carAgent)

            if simulation.allActorsDid() then
              simulation.resetCounterActors()
              simulation.actors().forEach(_ ! CarActor.StepAct(context.self, simulation))
            Behaviors.same

          case EndStepActCar(carAgent) =>
            simulation.increaseCounterActors()
            simulation.roadStatistics().addCarSpeed(carAgent)

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