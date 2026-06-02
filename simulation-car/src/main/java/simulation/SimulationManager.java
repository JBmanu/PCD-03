package simulation;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import view.simulation.SimulationView;
import actors.SimulationActor;

public class SimulationManager {

    public enum State { IDLE, RUNNING, PAUSED, ENDED }

    private State state;
    private SimulationType simulationType;
    private ActorRef<SimulationActor.Command> actorSystem;
    private final SimulationView view;
    private int simulationCounter;

    public SimulationManager() {
        this.state = State.IDLE;
        this.simulationType = SimulationType.SINGLE_ROAD;
        this.simulationCounter = 0;
        this.actorSystem = null;
        this.view = new SimulationView(this);
        this.view.onIdle();
    }

    public SimulationType simulationType() {
        return this.simulationType;
    }

    public State state() {
        return this.state;
    }

    public void changeSimulation(final SimulationType simulationType) {
        if (this.actorSystem != null) {
            this.actorSystem.tell(SimulationActor.Stop$.MODULE$);
            this.actorSystem = null;
        }
        this.simulationType = simulationType;
        this.state = State.IDLE;
        this.view.onIdle();
    }

    public void start(final int steps) {
        if (this.state != State.IDLE) return;

        this.simulationCounter++;
        this.state = State.RUNNING;

        final AbstractSimulation simulation = this.simulationType.getSimulation();
        simulation.addViewListener(this.view);

        this.actorSystem = ActorSystem.apply(
                SimulationActor.apply(simulation),
                "Simulation-" + this.simulationCounter);

        this.view.setupCommandsSimulation(this.actorSystem);
        this.actorSystem.tell(new SimulationActor.Start(steps));
        this.view.onRunning();
    }

    public void pause() {
        if (this.state != State.RUNNING) return;
        this.state = State.PAUSED;
        this.actorSystem.tell(SimulationActor.Pause$.MODULE$);
        this.view.onPaused();
    }

    public void resume() {
        if (this.state != State.PAUSED) return;
        this.state = State.RUNNING;
        this.actorSystem.tell(SimulationActor.Resume$.MODULE$);
        this.view.onRunning();
    }

    public void stop() {
        if (this.actorSystem != null) {
            this.actorSystem.tell(SimulationActor.Stop$.MODULE$);
            this.actorSystem = null;
        }
        this.state = State.ENDED;
        this.view.onEnded();
    }

    public void onSimulationEnded() {
        this.state = State.IDLE;
        this.view.onIdle();
    }
}