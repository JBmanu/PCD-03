package simulation;

import actors.CarActor;
import akka.actor.typed.ActorRef;
import inspector.RoadSimStatistics;
import actors.core.Engine;
import road.AbstractEnvironment;

import java.util.List;

public interface InspectorSimulation {

    Engine engine();

    RoadSimStatistics roadStatistics();

    AbstractEnvironment environment();

    List<ActorRef<CarActor.Command>> actors();

    void setup();
}
