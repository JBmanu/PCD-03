package view.inspector.setting;

import akka.actor.typed.ActorRef;
import actors.SimulationActor;

public interface StartStopViewListener {

    boolean conditionToStart(final ActorRef<SimulationActor.Command> simulation);

    void onStart(final ActorRef<SimulationActor.Command> simulation);

    void reset(final ActorRef<SimulationActor.Command> simulation);
}
