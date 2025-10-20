package testAkka

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import model.core.Times.TickScheduler
import testAkka.LoopTimer.Simulation
import testAkka.LoopTimer.Simulation.{ ActionAgent, SimulationCommand }

object Agent:

  sealed trait AgentCommand

  case class Start(simulation: ActorRef[SimulationCommand], timer: TickScheduler, agents: Set[ActorRef[AgentCommand]]) extends AgentCommand

  def apply(): Behavior[AgentCommand] =
    Behaviors.setup: context =>
      Behaviors.receiveMessage:
        case Start(simulation, timer, agents) =>
//          context.log.info(s"[AGENT] received Start action and replay con timer $timer")
          simulation ! ActionAgent(timer, agents)
          Behaviors.same
        case _                        =>
          context.log.warn("Received unknown action")
          Behaviors.unhandled