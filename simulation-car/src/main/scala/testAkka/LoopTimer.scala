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

    case class Start() extends SimulationCommand

    private case class Step() extends SimulationCommand

    case class Pause() extends SimulationCommand

    case class Stop() extends SimulationCommand

    case class ActionAgent() extends SimulationCommand

    def apply(): Behavior[SimulationCommand] =

      def withState(engine: Engine, agents: Set[ActorRef[AgentCommand]], counter: Int): Behavior[SimulationCommand] =
        Behaviors.setup: context =>
          Behaviors.withTimers: timers =>
            Behaviors.receiveMessage:
              case Start() =>
                context.log.info(s"[SIM] START ciclo con timer di $engine")
                context.self ! Step()
                withState(engine.start(), agents, counter)

              case Step() =>
                val newEngine = engine.setSystemCurrentTime()
                context.log.info(s"[SIM] STEP con timer di $newEngine")
                agents.foreach(_ ! Agent.Start(context.self, newEngine, agents))

                // manca cambiare la variabiele is pause in true
                withState(newEngine, agents, counter)

              case Pause() =>
                withState(engine.pause(), agents, counter)

              case Stop() =>
                val newEngine = engine.stop()
                context.log.info("[SIM] END -> TOTAL TIME: " + newEngine.allTimeSpent + " AVERAGE TIME: " + newEngine.averageTimeForStep())
                withState(newEngine, agents, counter)

              case ActionAgent() =>
                val newCounter = counter + 1
                if newCounter equals agents.size then
                  context.log.info(s"[SIM] ALL AGENT FINISH")
                  val newEngine = engine.nextStep().setSystemCurrentTime()
                  if !engine.isInPause then
                    if newEngine.hasMoreSteps then
                      engine.computeDelay() match
                        case Some(value: Long) =>
                          context.log.info(s"[SIM] TICK con timer: $engine e delay: $value")
                          timers.startSingleTimer(Step(), value.millis)
                        case None              =>
                          context.log.info(s"[SIM] TICK con timer: $engine, ma nessun delay calcolato")
                          context.self ! Step()
                    else
                      context.self ! Stop()
                  withState(newEngine, agents, 0)
                else
                  withState(engine, agents, newCounter)

      val agents: Set[ActorRef[AgentCommand]] = (1 to 5).map(i => ActorSystem(Agent(), s"AgentSystem$i")).toSet
      withState(Engine(1, 100, 10), agents, 0)


  def main(args: Array[String]): Unit =
    val simulation: ActorRef[SimulationCommand] = ActorSystem(Simulation(), "SIMULATION")
    simulation ! Simulation.Start()

    Thread.sleep(3000) // attende 2000 millisecondi = 2 secondi
    simulation ! Simulation.Pause()
    Thread.sleep(3000) // attende 2000 millisecondi = 2 secondi
    simulation ! Simulation.Start()
