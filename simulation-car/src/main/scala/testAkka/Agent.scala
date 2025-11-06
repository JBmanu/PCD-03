package testAkka

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, Behavior }
import testAkka.LoopTimer.Simulation
import testAkka.LoopTimer.Simulation.{ ActionAgent, SimulationCommand }
import wrapper.core.Engine

object Agent:

  case class AgentState(currentCommand: Int, commands: List[Int]):

    def nextCommand(): AgentState = copy(commands((currentCommand + 1) % commands.size))

  sealed trait AgentCommand

  case class Start(simulation: ActorRef[SimulationCommand], engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends AgentCommand

  def apply(): Behavior[AgentCommand] =

    def withState(state: AgentState): Behavior[AgentCommand] =
      Behaviors.setup: context =>
        Behaviors.receiveMessage:
          case Start(simulation, engine, agents) =>
            context.log.info(s"[AGENT] activer at time ${System.currentTimeMillis()} e do command ${state.currentCommand}")
            simulation ! ActionAgent(state.currentCommand)
            withState(state.nextCommand())
          case _                                 =>
            context.log.warn("Received unknown action")
            Behaviors.unhandled

    withState(AgentState(0, List(0, 1, 2)))