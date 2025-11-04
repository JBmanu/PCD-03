package model.actors


import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import model.simulation.Simulation

import scala.concurrent.duration.*

object ActorSimulation:


  sealed trait Command

  object Start extends Command

  object Step extends Command

  object Pause extends Command

  object Stop extends Command

  case class CallCars(carStep: Int) extends Command

  def apply(simulation: Simulation): Behavior[Command] =
    Behaviors.setup: context =>
      Behaviors.withTimers: timers =>
        Behaviors.receiveMessage:
          case Start                  =>
            context.log.info("START")
            context.self ! Step
            ActorSimulation(simulation)

          case Step                   =>
            // chiamata agli agenti
//            AgentSimulation(simulation.step())
            context.log.info("STEP")
            ActorSimulation(simulation)

          case Pause                  => ActorSimulation(simulation.pause())

          case Stop                   => ActorSimulation(simulation.stop())

          case CallCars(carStep: Int) => ActorSimulation(simulation)


  def main(args: Array[String]): Unit =
    val simulation = Simulation()
    simulation.setup()

    val agentSimulation: ActorSystem[Command] = ActorSystem(ActorSimulation(simulation), "Simulation")
    agentSimulation ! Start