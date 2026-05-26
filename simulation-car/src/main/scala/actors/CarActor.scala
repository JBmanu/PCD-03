package actors

import actors.SimulationActor.{ EndInitCar, EndSenseDecide, EndStepCar }
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import car.{ AbstractAgent, CarAgent }
import simulation.AbstractSimulation

object CarActor:

  sealed trait Command

  case class Init(actor: ActorRef[SimulationActor.Command], simulation: AbstractSimulation) extends Command

  case class StepSenseDecide(actor: ActorRef[SimulationActor.Command], simulation: AbstractSimulation) extends Command

  case class SepAct(actor: ActorRef[SimulationActor.Command]) extends Command

  def apply(agent: AbstractAgent): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:

          // inizializza attore della macchina
          case Init(actor, simulation) =>
            agent.init(simulation.environment())
            actor ! EndInitCar
            Behaviors.same

          // esegue sense e decide
          // cosi prima di muoversi tutti sanno cosa fare a quel tempo
          case StepSenseDecide(actor, simulation) =>
            agent.step(simulation.engine().delta)
            actor ! EndSenseDecide(agent.asInstanceOf[CarAgent])
            Behaviors.same

          // una volta calcolata decide fanno ACT deterministica
          case SepAct(actor) =>
            agent.act()
            actor ! EndStepCar(agent.asInstanceOf[CarAgent])
            Behaviors.same
