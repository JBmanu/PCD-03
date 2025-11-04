package model.actors


import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import model.actors.ActorCar.Play
import model.simulation.Simulation

import scala.concurrent.duration.DurationLong

object ActorSimulation:


  sealed trait Command

  object Start extends Command

  object NextStep extends Command

  object Pause extends Command

  object Stop extends Command

  case class CallCars(carStep: Int) extends Command

  def apply(simulation: Simulation): Behavior[Command] =

    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Start =>
            context.log.info("START")
            val newSimulation = simulation.start()
            newSimulation.actorCars().foreach(_ ! Play(context.self, newSimulation))
            ActorSimulation(newSimulation)

          case NextStep =>
            context.log.info("STEP")
            val newSimulation = simulation.step()
            newSimulation.actorCars().foreach(_ ! Play(context.self, newSimulation))
            ActorSimulation(newSimulation)

          case Pause => ActorSimulation(simulation.pause())

          case Stop => ActorSimulation(simulation.stop())

          case CallCars(carStep: Int) =>
            simulation.counterCars = simulation.counterCars() + 1
            if simulation.counterCars() equals simulation.actorCars().size then
              if !simulation.engine().isInPause then
                if simulation.engine().hasMoreSteps then
                  simulation.engine().computeDelay() match
                    case Some(value) => timers.startSingleTimer(NextStep, value.millis)
                    case None        => context.self ! NextStep
                else
                  context.self ! Stop
            Behaviors.same


  def main(args: Array[String]): Unit =
    val simulation = Simulation()
    val agentSimulation: ActorSystem[Command] = ActorSystem(ActorSimulation(simulation), "Simulation")
    agentSimulation ! Start