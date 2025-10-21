package testAkka

import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import model.core.Engine
import testAkka.Agent.AgentCommand
import testAkka.LoopTimer.Simulation.SimulationCommand

import scala.concurrent.duration.*

object LoopTimer:
  object Simulation:

    sealed trait SimulationCommand

    case class Start(engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    private case class Step(engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    case class Pause(engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    case class Stop(engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    case class ActionAgent(engine: Engine, agents: Set[ActorRef[AgentCommand]]) extends SimulationCommand

    def apply(): Behavior[SimulationCommand] =
      def withState(counter: Int): Behavior[SimulationCommand] =
        Behaviors.setup: context =>
          Behaviors.withTimers: timers =>
            Behaviors.receiveMessage:
              case Start(engine, agents) =>
                context.log.info(s"[SIM] START ciclo con timer di $engine")
                context.self ! Step(engine, agents)
                Behaviors.same

              case Step(engine, agents) =>
                val newEngine = engine.setSystemCurrentTime()
                context.log.info(s"[SIM] STEP con timer di $newEngine")
                agents.foreach(_ ! Agent.Start(context.self, newEngine, agents))
                Behaviors.same

              case Pause(engine, agents) =>

                Behaviors.same

              case Stop(engine, agents) =>
                val newEngine = engine.stop()
                context.log.info("FINAL ENGINE: " + newEngine)
                context.log.info("TOTAL TIME: " + newEngine.allTimeSpent + " AVERAGE TIME: " + newEngine.averageTimeForStep())
                Behaviors.same

              case ActionAgent(engine, agents) =>
                val newCounter = counter + 1
                if newCounter equals agents.size then
                  context.log.info(s"[SIM] ALL AGENT FINISH")
                  val newEngine = engine.nextStep().setSystemCurrentTime()
                  if newEngine.hasMoreSteps then
                    engine.computeDelay() match
                      case Some(value: Long) =>
                        context.log.info(s"[SIM] TICK con timer: $engine e delay: $value")
                        timers.startSingleTimer(Step(newEngine, agents), value.millis)
                      case None              =>
                        context.log.info(s"[SIM] TICK con timer: $engine, ma nessun delay calcolato")
                        context.self ! Step(newEngine, agents)
                  else
                    context.self ! Stop(newEngine, agents)
                  withState(0)
                else
                  withState(newCounter)

      withState(0)


  def main(args: Array[String]): Unit =
    val agents: Set[ActorRef[AgentCommand]] = (1 to 5).map(i => ActorSystem(Agent(), s"AgentSystem$i")).toSet
    val simulation: ActorRef[SimulationCommand] = ActorSystem(Simulation(), "SIMULATION")
    val engine: Engine = Engine()
    simulation ! Simulation.Start(engine.start(1, 100, 10), agents)
