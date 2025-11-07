package inspector;

import actors.CarActor;
import akka.actor.typed.ActorRef;
import simulation.InspectorSimulation;
import simulation.listener.ModelSimulationListener;

import java.util.List;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements ModelSimulationListener {
    private double currentAverageSpeed;
    private double averageSpeed;
    private double minSpeed;
    private double maxSpeed;

    public RoadSimStatistics() {
    }

    public void updateAverageSpeed(final double currSpeed, final int totalActors) {
        this.currentAverageSpeed += currSpeed;
        if (currSpeed > this.maxSpeed) {
            this.maxSpeed = currSpeed;
        } else if (currSpeed < this.minSpeed) {
            this.minSpeed = currSpeed;
        }

        if (totalActors > 0) {
            this.currentAverageSpeed /= totalActors;
        }
    }

    public double currentAverageSpeed() {
        return this.currentAverageSpeed;
    }

    public double averageSpeed() {
        return this.averageSpeed;
    }

    public double minSpeed() {
        return this.minSpeed;
    }

    public double maxSpeed() {
        return this.maxSpeed;
    }

    @Override
    public void notifyInit(final int t, final InspectorSimulation simulation) {
        // log("reset: " + t);
        this.averageSpeed = 0;
        this.currentAverageSpeed = 0;
    }

    @Override
    public void notifyStepDone(final int t, final InspectorSimulation simulation) {
        this.currentAverageSpeed = 0;

        this.maxSpeed = -1;
        this.minSpeed = Double.MAX_VALUE;
        final List<ActorRef<CarActor.Command>> actors = simulation.actors();
//        actors.forEach(actor -> actor.tell(CarActor.UpdateStats.apply(this)));

//        for (final var agent : actors) {
//            final CarAgent car = (CarAgent) agent;
//            final double currSpeed = car.getCurrentSpeed();
//            this.currentAverageSpeed += currSpeed;
//            if (currSpeed > this.maxSpeed) {
//                this.maxSpeed = currSpeed;
//            } else if (currSpeed < this.minSpeed) {
//                this.minSpeed = currSpeed;
//            }
//        }
//
//        if (!actors.isEmpty()) {
//            this.currentAverageSpeed /= actors.size();
//        }
//        this.log("average speed: " + this.currentAverageSpeed);
    }

    @Override
    public void notifyEnd(final InspectorSimulation simulation) {

    }

    private void log(final String msg) {
        System.out.println("[STAT] " + msg);
    }

}
