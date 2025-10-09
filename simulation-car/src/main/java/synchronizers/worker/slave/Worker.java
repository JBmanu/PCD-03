package synchronizers.worker.slave;

import car.command.CarCommand;

import java.util.concurrent.Callable;

public interface Worker extends Callable<Void> {

    void setCarCommand(CarCommand command);

}
