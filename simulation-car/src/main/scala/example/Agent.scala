package example

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import model.core.Times.Time
import example.LoopTimer.Simulation
import example.LoopTimer.Simulation.{ ActionAgent, SimulationCommand }

object Agent:

  sealed trait AgentCommand

  case class Start(simulation: ActorRef[SimulationCommand], timer: Time, agents: Set[ActorRef[AgentCommand]]) extends AgentCommand

  def apply(): Behavior[AgentCommand] =
    Behaviors.setup: context =>
      Behaviors.receiveMessage:
        case Start(simulation, timer, agents) =>
          context.log.info(s"[AGENT] received Start action and replay con timer $timer")
          simulation ! ActionAgent(timer, agents)
          Behaviors.same
        case _                        =>
          context.log.warn("Received unknown action")
          Behaviors.unhandled