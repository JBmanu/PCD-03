import Agent.AgentCommand
import LoopTimer.Simulation.SimulationCommand
import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import model.core.Times.Time

import scala.concurrent.duration.*

object LoopTimer:
  object Simulation:

    sealed trait SimulationCommand

    case class Start(timer: Time, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    private case class Step(timer: Time, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    case class ActionAgent(timer: Time, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    def apply(): Behavior[SimulationCommand] =
      def withState(counter: Int): Behavior[SimulationCommand] =
        Behaviors.setup: context =>
          Behaviors.withTimers: timers =>
            Behaviors.receiveMessage:
              case Start(timer, agents) =>
                val newTimer = timer.setCurrentSystem
                context.log.info(s"[SIM] START ciclo con timer di $newTimer")
                context.self ! Step(newTimer, agents)
                Behaviors.same

              case Step(timer, agents) =>
                context.log.info(s"[SIM] STEP con timer di $timer")
                agents.foreach(_ ! Agent.Start(context.self, timer, agents))
                Behaviors.same

              case ActionAgent(timer, agents) =>
                val newCounter = counter + 1
                if newCounter equals agents.size then
                  context.log.info(s"[SIM] ALL AGENT FINISH")
                  val newTimer = timer.nextStep.setCurrentSystem
                  timer.computeDelay match
                    case Some(value: Long) =>
                      context.log.info(s"[SIM] TICK con timer: $timer e delay: $value")
                      timers.startSingleTimer(Step(newTimer, agents), value.millis)
                    case None              =>
                      context.log.info(s"[SIM] TICK con timer: $timer, ma nessun delay calcolato")
                      context.self ! Step(newTimer, agents)
                  withState(0)
                else
                  withState(newCounter)

      withState(0)


  def main(args: Array[String]): Unit =
    val agents: Set[ActorRef[AgentCommand]] = (1 to 5).map(i => ActorSystem(Agent(), s"AgentSystem$i")).toSet
    val simulation: ActorRef[SimulationCommand] = ActorSystem(Simulation(), "SIMULATION")
    val timer: Time = Time(2, 100)
    simulation ! Simulation.Start(timer, agents)
