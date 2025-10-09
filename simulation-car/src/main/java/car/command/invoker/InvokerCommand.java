package car.command.invoker;

import car.command.CommandCar;

public interface InvokerCommand {

    void setup(final int dt);

    void execute(final CommandCar command);

}
