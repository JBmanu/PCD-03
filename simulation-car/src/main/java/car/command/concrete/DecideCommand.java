package car.command.concrete;

import car.CarAgent;
import car.command.CarCommand;

import java.util.Optional;

public class DecideCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.setSelectedAction(Optional.empty());
        carAgent.decide();
    }
}
