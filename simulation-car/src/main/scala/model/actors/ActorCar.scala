package model.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import model.car.CarAgent
import model.simulation.Simulation

object ActorCar:

  sealed trait Command

  case class Play(simulation: Simulation) extends Command

  def apply(carAgent: CarAgent): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.receiveMessage:
        case Play(simulation: Simulation) =>
          Behaviors.same

        case _ =>
          context.log.warn("error command not found")
          Behaviors.same
