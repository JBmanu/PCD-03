package model.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }
import model.actors.ActorSimulation.CallCars
import model.car.CarAgent
import model.road.Environment
import model.simulation.Simulation

object ActorCar:

  sealed trait Command

  case class Init(environment: Environment) extends Command

  case class Play(simulationActor: ActorRef[ActorSimulation.Command], simulation: Simulation) extends Command

  case class Request(requester: ActorSystem[Command]) extends Command

  case class Send(car: CarAgent) extends Command


  def apply(carAgent: CarAgent): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.receiveMessage:
        case Init(environment: Environment) =>
          carAgent.init(environment)
          Behaviors.same

        case Play(actorSimulation: ActorRef[ActorSimulation.Command], simulation: Simulation) =>
          carAgent.step(simulation.engine().currentTick)
          actorSimulation ! CallCars(1)
          Behaviors.same

        case Request(requester: ActorSystem[Command]) =>
          requester ! Send(carAgent)
          Behaviors.same

        case _ =>
          context.log.warn("error command not found")
          Behaviors.same
