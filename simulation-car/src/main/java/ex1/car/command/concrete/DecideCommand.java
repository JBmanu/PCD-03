package ex1.car.command.concrete;

import ex1.car.CarAgent;
import ex1.car.command.CarCommand;

import java.util.Optional;

public class DecideCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.setSelectedAction(Optional.empty());
        carAgent.decide();
    }
}
