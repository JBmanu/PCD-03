package synchronizers.worker.master;

import car.CarAgent;
import car.command.CarCommand;
import synchronizers.service.CommandService;
import synchronizers.service.CommandServiceImpl;
import synchronizers.worker.slave.Worker;
import synchronizers.worker.slave.WorkerCarBarrier;
import utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MultiWorkerGeneric extends BaseMasterWorker implements MasterWorker {
    private final List<Worker> carsWorkers;
    private final CommandService commandService;
    private final int divisor;

    public MultiWorkerGeneric(final ExecutorService executorService, final int divisor) {
        super(executorService);
        this.carsWorkers = new ArrayList<>();
        this.commandService = new CommandServiceImpl(this);
        this.divisor = divisor;
    }

    public MultiWorkerGeneric(final int divisor) {
        this.carsWorkers = new ArrayList<>();
        this.commandService = new CommandServiceImpl(this);
        this.divisor = divisor;
    }

    public MultiWorkerGeneric() {
        this(5);
    }

    @Override
    public void setup() {
        final List<List<CarAgent>> carDividedList = ListUtils.divideEqually(this.carAgents(), this.divisor);
        this.commandService.setup(carDividedList.size());
        carDividedList.forEach(car -> this.carsWorkers.add(new WorkerCarBarrier(car)));
    }

    @Override
    public void callNextTaskCommand() {
        final CarCommand command = this.nextCommand();
        System.out.println("\nRUN COMMAND: " + command.getClass().getSimpleName());
        this.carsWorkers.forEach(worker -> worker.setCarCommand(command));
        this.commandService.runTask(this.runTask(this.carsWorkers));
    }

}
