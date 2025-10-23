package testAkka

import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import model.core.Engine
import testAkka.Agent.AgentCommand
import testAkka.LoopTimer.Simulation.SimulationCommand

import scala.concurrent.duration.*

object LoopTimer:


  case class SimulationState(engine: Engine, agents: Set[ActorRef[AgentCommand]], counter: Int):

    def updateEngine(update: Engine => Engine): SimulationState = copy(update(engine))

    def updateAgents(update: Set[ActorRef[AgentCommand]] => Set[ActorRef[AgentCommand]]): SimulationState = copy(agents = update(agents))

    def updateCounter(update: Int => Int): SimulationState = copy(counter = update(counter))


  object Simulation:

    sealed trait SimulationCommand

    object Start extends SimulationCommand

    private case class Step() extends SimulationCommand

    object Pause extends SimulationCommand

    case class Stop() extends SimulationCommand

    case class ActionAgent() extends SimulationCommand

    def apply(): Behavior[SimulationCommand] =

      def withState(state: SimulationState): Behavior[SimulationCommand] =
        Behaviors.setup: context =>
          Behaviors.withTimers: timers =>
            Behaviors.receiveMessage:
              case Start =>
                context.log.info(s"[SIM] START ciclo con timer di ${state.engine}")
                context.self ! Step()
                withState(state.updateEngine(_.start()))

              case Step() =>
                val newEngine = state.engine.nextStep()
                context.log.info(s"[SIM] STEP con timer di $newEngine")
                state.agents.foreach(_ ! Agent.Start(context.self, newEngine, state.agents))
                withState(state.updateEngine(_ => newEngine))

              case Pause => withState(state.updateEngine(_.pause()))

              case Stop() =>
                val newEngine = state.engine.stop()
                context.log.info("[SIM] END -> TOTAL TIME: " + newEngine.allTimeSpent + " AVERAGE TIME: " + newEngine.averageTimeForStep())
                withState(state.updateEngine(_ => newEngine))

              case ActionAgent() =>
                val newCounter = state.counter + 1
                if newCounter equals state. agents.size then
                  context.log.info(s"[SIM] ALL AGENT FINISH")
                  if !state.engine.isInPause then
                    if state.engine.hasMoreSteps then
                      state.engine.computeDelay() match
                        case Some(value: Long) =>
                          context.log.info(s"[SIM] TICK con timer: ${state.engine} e delay: $value")
                          timers.startSingleTimer(Step(), value.millis)
                        case None              =>
                          context.log.info(s"[SIM] TICK con timer: ${state.engine}, ma nessun delay calcolato")
                          context.self ! Step()
                    else
                      context.self ! Stop()
                  withState(state.updateCounter(_ => 0))
                else
                  withState(state.updateCounter(_ => newCounter))

      val agents: Set[ActorRef[AgentCommand]] = (1 to 5).map(i => ActorSystem(Agent(), s"AgentSystem$i")).toSet
      withState(SimulationState(Engine(1, 100, 10), agents, 0))


  def main(args: Array[String]): Unit =
    val simulation: ActorRef[SimulationCommand] = ActorSystem(Simulation(), "SIMULATION")
    simulation ! Simulation.Start

    Thread.sleep(3000) // attende 2000 millisecondi = 2 secondi
    simulation ! Simulation.Pause
    Thread.sleep(3000) // attende 2000 millisecondi = 2 secondi
    simulation ! Simulation.Start
