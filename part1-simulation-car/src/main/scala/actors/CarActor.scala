package actors

import actors.SimulationActor.{ EndInitCar, EndStepActCar, EndStepSenseDecideCar }
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import car.{ AbstractAgent, CarAgent }
import simulation.AbstractSimulation

object CarActor:

  sealed trait Command

  case class Init(actor: ActorRef[SimulationActor.Command], simulation: AbstractSimulation) extends Command

  case class StepSenseDecide(actor: ActorRef[SimulationActor.Command], simulation: AbstractSimulation) extends Command

  case class StepAct(actor: ActorRef[SimulationActor.Command], simulation: AbstractSimulation) extends Command

  def apply(agent: AbstractAgent): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Init(actor, simulation) =>
            agent.init(simulation.environment())
            actor ! EndInitCar
            Behaviors.same

          case StepSenseDecide(actor, simulation) =>
            agent.step(simulation.engine().delta)
            actor ! EndStepSenseDecideCar(agent.asInstanceOf[CarAgent])
            Behaviors.same

          case StepAct(actor, simulation) =>
            agent.act()
            actor ! EndStepActCar(agent.asInstanceOf[CarAgent])
            Behaviors.same