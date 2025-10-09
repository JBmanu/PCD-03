package ex1.car.command.concrete;

import ex1.car.CarAgent;
import ex1.car.CarPercept;
import ex1.car.command.CarCommand;

public class SenseCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.setCurrentPercept((CarPercept) carAgent.getCurrentPercepts());
    }
}
