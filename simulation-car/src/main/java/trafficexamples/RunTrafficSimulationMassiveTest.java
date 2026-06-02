package trafficexamples;

import actors.SimulationActor;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public final class RunTrafficSimulationMassiveTest {

    public static void main(final String[] args) {
        final int numCars = 5000;
        final int nSteps = 100;

        final var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars);

        log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");

        final ActorRef<SimulationActor.Command> actorSystem =
                ActorSystem.apply(SimulationActor.apply(simulation), "MassiveSimulation");

        actorSystem.tell(new SimulationActor.Start(nSteps));
    }

    private static void log(final String msg) {
        System.out.println("[ SIMULATION ] " + msg);
    }
}
