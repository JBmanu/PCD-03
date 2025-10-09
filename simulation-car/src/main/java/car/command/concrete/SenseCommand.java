package car.command.concrete;

import car.CarAgent;
import car.CarPercept;
import car.command.CarCommand;

public class SenseCommand implements CarCommand {

    @Override
    public void execute(final CarAgent carAgent) {
        carAgent.setCurrentPercept((CarPercept) carAgent.getCurrentPercepts());
    }
}
