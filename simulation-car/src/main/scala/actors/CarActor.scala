package actors

import actors.SimulationActor.{ EndInitCar, EndStepCar }
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import car.{ AbstractAgent, CarAgent }
import simulation.AbstractSimulation

object CarActor:

  sealed trait Command

  case class Init(actor: ActorRef[SimulationActor.Command], data: AbstractSimulation) extends Command

  case class Step(actor: ActorRef[SimulationActor.Command], data: AbstractSimulation) extends Command


  def apply(agent: AbstractAgent): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Init(actor, data) =>
            agent.init(data.environment())
            actor ! EndInitCar
            Behaviors.same

          case Step(actor, data) =>
            agent.step(data.engine().delta)
            actor ! EndStepCar(agent.asInstanceOf[CarAgent])
            Behaviors.same
