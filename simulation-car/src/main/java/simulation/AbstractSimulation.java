package simulation;

import car.AbstractAgent;
import inspector.RoadSimStatistics;
import inspector.Stepper;
import inspector.TimeStatistics;
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

    /* list of the agents */
    private final List<AbstractAgent> agents;

    /* logical time step */
    private int dt;
    /* initial logical time */
    private int t0;
    private int t;
    private long timePerStep;

    /* in the case of sync with wall-time */
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;

    /* simulation listeners */
    private final List<ModelSimulationListener> modelListeners;
    private final List<ViewSimulationListener> viewListeners;

    // Model
//    private final StartStopMonitor startStopMonitorSimulation;
    private final RoadSimStatistics roadStatistics;
    private final TimeStatistics timeStatistics;
    private final Stepper stepper;
    
    private boolean isPause; 


    protected AbstractSimulation() {
        this.agents = new ArrayList<>();
        this.modelListeners = new ArrayList<>();
        this.viewListeners = new ArrayList<>();

//        this.startStopMonitorSimulation = new StartStopMonitorImpl();
        this.roadStatistics = new RoadSimStatistics();
        this.timeStatistics = new TimeStatistics();
        this.stepper = new Stepper();

        this.isPause = true;
        this.toBeInSyncWithWallTime = false;
        this.setupModelListener();
//        this.startStopMonitorSimulation.pause();
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
    public Stepper stepper() {
        return this.stepper;
    }

    @Override
    public TimeStatistics timeStatistics() {
        return this.timeStatistics;
    }

    @Override
    public RoadSimStatistics roadStatistics() {
        return this.roadStatistics;
    }

    public void play() {
        this.isPause = false;
    }

    public void pause() {
        this.isPause = true;
    }

    public boolean isPause() {
        return this.isPause;
    }

    public void init() {
        /* initialize the env and the agents inside */
        this.play();

        this.t = this.t0;
        this.timePerStep = 0;
        this.timeStatistics.setStartWallTime();

        this.env.init();
        this.agents.forEach(agent -> agent.init(this.env));
        this.notifyInit(this.t);
    }
    
    public void nextStep() {
        this.timeStatistics.setCurrentWallTime(System.currentTimeMillis());

        /* make a step */
        this.env.step(this.dt);
        for (final var agent : this.agents) {
            agent.step(this.dt);
        }

        this.t += this.dt;

        this.notifyStepDone(this.t);

        this.stepper.increaseStep();
        this.timePerStep += System.currentTimeMillis() - this.timeStatistics.currentWallTime();
    }
    
    public void end() {
        this.timeStatistics.setEndWallTime(System.currentTimeMillis());
        this.timeStatistics.setAverageTimeForStep((double) this.timePerStep / this.stepper.totalStep());

        System.out.println("COMPLETED IN: " + this.timeStatistics().totalWallTime() + " ms");
        System.out.println("AVERAGE TIME PER STEP: " + this.timeStatistics().averageTimeForStep() + " ms");
        this.notifyEnd();
    }
    
    /**
     * Method running the simulation for a number of steps,
     * using a sequential approach
     */
//    @Override
//    public void run() {
//        while (this.isPause) {
//            try {
//                Thread.sleep(10);
//            } catch (final InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
////        this.startStopMonitorSimulation.awaitUntilPlay();
//
//        /* initialize the env and the agents inside */
//        this.init();
//
//        while (this.stepper.hasMoreSteps()) {
//            while (this.isPause) {
//                try {
//                    Thread.sleep(10);
//                } catch (final InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
////            this.startStopMonitorSimulation.awaitUntilPlay();
//            this.nextStep();
//            if (this.toBeInSyncWithWallTime) {
//                this.syncWithWallTime();
//            }
//        }
//        
//        this.end();
//    }

    /* methods for configuring the simulation */
    protected void setupTimings(final int t0, final int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(final int nCyclesPerSec) {
        this.toBeInSyncWithWallTime = true;
        this.nStepsPerSec = nCyclesPerSec;
    }

    protected void setupEnvironment(final AbstractEnvironment env) {
        this.env = env;
    }

    protected void addAgent(final AbstractAgent agent) {
        this.agents.add(agent);
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

    public void notifyStepDone(final int t) {
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

    /* method to sync with wall time at a specified step rate */

    private void syncWithWallTime() {
        try {
            final long newWallTime = System.currentTimeMillis();
            final long delay = 1000 / this.nStepsPerSec;
            final long wallTimeDT = newWallTime - this.timeStatistics.currentWallTime();
            if (wallTimeDT < delay) {
                Thread.sleep(delay - wallTimeDT);
            }
        } catch (final Exception ex) {
        }
    }


}
