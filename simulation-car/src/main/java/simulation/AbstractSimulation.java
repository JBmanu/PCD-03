package simulation;

import akka.actor.typed.ActorRef;
import car.AbstractAgent;
import inspector.RoadSimStatistics;
import wrapper.CarActor;
import wrapper.core.Engine;
import wrapper.core.Scheduler;
import road.AbstractEnvironment;
import simulation.listener.ModelSimulationListener;
import view.simulation.ViewSimulationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for defining concrete simulations
 */
public abstract class AbstractSimulation implements InspectorSimulation {

    /* environment of the simulation */
    private AbstractEnvironment env;
    
    private final List<ActorRef<CarActor.Command>> actors; 
    private int counterActors;
    
    /* list of the agents */
    private final List<AbstractAgent> agents;

    // engine to control simulation
    private Engine engine;
    // Statistics cars
    private final RoadSimStatistics roadStatistics;

    /* simulation listeners */
    private final List<ModelSimulationListener> modelListeners;
    private final List<ViewSimulationListener> viewListeners;


    protected AbstractSimulation() {
        this.agents = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.counterActors = 0;
        
        this.modelListeners = new ArrayList<>();
        this.viewListeners = new ArrayList<>();
        this.engine = Engine.empty();

        this.roadStatistics = new RoadSimStatistics();

        this.setupModelListener();
    }

    /**
     * Method used to configure the simulation, specifying env and agents
     */
    public abstract void setup();

    private void setupModelListener() {
        this.addModelListener(this.roadStatistics);
    }

    @Override
    public AbstractEnvironment environment() {
        return this.env;
    }

    @Override
    public List<AbstractAgent> agents() {
        return this.agents;
    }

    @Override
    public Engine engine() {
        return this.engine;
    }

    @Override
    public RoadSimStatistics roadStatistics() {
        return this.roadStatistics;
    }

    public List<ActorRef<CarActor.Command>> actors() {
        return this.actors;
    }
    
    public void increaseCounterActors() {
        this.counterActors++;
    }
    
    public void resetCounterActors() {
        this.counterActors = 0;
    }
    
    public boolean allActorsDid() {
        return this.counterActors == this.actors.size();
    }

    public void pause() {
        this.engine = this.engine.pause();
    }

    public void resume() {
        this.engine = this.engine.resume();
    }

    public boolean isPause() {
        return this.engine.isInPause();
    }

    public void start(final int totalSteps) {
        /* initialize the env and the agents inside */
        this.engine = this.engine.setTotalSteps(totalSteps);
        this.engine = this.engine.start();
        this.env.init();
        this.env.step(this.engine.delta());
        this.notifyInit(this.engine.currentTick());
    }
    
    public void nextStep() {
        /* make a step */
        this.engine = this.engine.nextStep();
        this.notifyStepDone(this.engine.currentTick());
        this.env.step(this.engine.delta());
    }

    public void end() {
        this.engine = this.engine.stop();
        System.out.println("COMPLETED IN: " + this.engine.allTimeSpent() + " ms");
        System.out.println("AVERAGE TIME PER STEP: " + this.engine.averageTimeForStep() + " ms");
        this.notifyEnd();
    }

    /* methods for configuring the simulation */
    protected void setupTimings(final int nCyclesPerSec, final int t0, final int dt) {
        this.engine = this.engine.buildSchedule(Scheduler.apply(nCyclesPerSec, t0, dt));
    }

    protected void setupEnvironment(final AbstractEnvironment env) {
        this.env = env;
    }

    protected void addAgent(final AbstractAgent agent) {
        this.agents.add(agent);
        this.actors.add(agent.actor());
    }

    // listener
    // adders
    public void addModelListener(final ModelSimulationListener listener) {
        this.modelListeners.add(listener);
    }

    public void addViewListener(final ViewSimulationListener listener) {
        this.viewListeners.add(listener);
    }

    // actions
    private void notifyInit(final int t0) {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyInit(t0, this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyInit(t0, this);
        }
    }

    private void notifyStepDone(final int t) {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyStepDone(t, this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyStepDone(t, this);
        }
    }

    private void notifyEnd() {
        // Model
        for (final var listener : this.modelListeners) {
            listener.notifyEnd(this);
        }
        // View
        for (final var listener : this.viewListeners) {
            listener.notifyEnd(this);
        }
    }

}
