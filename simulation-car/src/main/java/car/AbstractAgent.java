package car;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import road.AbstractEnvironment;
import simulation.engineseq.Action;
import simulation.engineseq.Percept;
import actors.CarActor;

/**
 * Base  class for defining types of agents taking part to the simulation
 */
public abstract class AbstractAgent {

    private final String myId;
    private AbstractEnvironment env;

    /**
     * Each agent has an identifier
     *
     * @param id
     */
    protected AbstractAgent(final String id) {
        this.myId = id;
    }

    /**
     * This method is called at the beginning of the simulation
     *
     * @param env
     */
    public void init(final AbstractEnvironment env) {
        this.env = env;
    }

    /**
     * This method is called at each step of the simulation
     */
    abstract public void step(int dt);

    public abstract void setTimeDt(final int dt);

    public String getId() {
        return this.myId;
    }

    protected AbstractEnvironment getEnv() {
        return this.env;
    }

    public Percept getCurrentPercepts() {
        return this.env.getCurrentPercepts(this.myId);
    }

    public void doAction(final Action action) {
        this.env.doAction(this.myId, action);
    }


    public ActorRef<CarActor.Command> actor() {
        return ActorSystem.apply(CarActor.apply(this), this.myId);
    }
}
