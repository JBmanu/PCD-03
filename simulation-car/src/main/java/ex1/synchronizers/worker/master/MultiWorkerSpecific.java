package ex1.synchronizers.worker.master;

import ex1.car.CarAgent;
import ex1.car.command.CarCommand;
import ex1.synchronizers.service.CommandService;
import ex1.synchronizers.service.CommandServiceImpl;
import ex1.synchronizers.worker.slave.Worker;
import ex1.synchronizers.worker.slave.WorkerCarBarrier;
import ex1.utils.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiWorkerSpecific extends BaseMasterWorker implements MasterWorker {
    private final Map<CarCommand, CommandService> commandServiceMap;
    private final Map<CarCommand, List<Worker>> carsWorkersMap;
    private final Map<CarCommand, Integer> commandDivisorMap;

    public MultiWorkerSpecific(final ExecutorService executorService, final int sense, final int decide, final int action) {
        super(executorService);
        this.carsWorkersMap = new HashMap<>();
        this.commandDivisorMap = this.carCommands().stream().collect(Collectors.toMap(command -> command, command -> 5));
        this.commandServiceMap = this.carCommands().stream().collect(Collectors.toMap(command -> command, command -> new CommandServiceImpl(this)));

        final List<Integer> divisor = List.of(sense, decide, action);
        IntStream.range(0, Math.min(3, this.totalCommands())).forEach(i -> this.commandDivisorMap.put(this.command(i), divisor.get(i)));
    }

    public MultiWorkerSpecific(final int sense, final int decide, final int action) {
        this.carsWorkersMap = new HashMap<>();
        this.commandDivisorMap = this.carCommands().stream().collect(Collectors.toMap(command -> command, command -> 5));
        this.commandServiceMap = this.carCommands().stream().collect(Collectors.toMap(command -> command, command -> new CommandServiceImpl(this)));

        final List<Integer> divisor = List.of(sense, decide, action);
        IntStream.range(0, Math.min(3, this.totalCommands())).forEach(i -> this.commandDivisorMap.put(this.command(i), divisor.get(i)));
    }

    public MultiWorkerSpecific() {
        this(5, 5, 5);
    }

    @Override
    public void setup() {
        this.carCommands().forEach(command -> {
            final List<List<CarAgent>> carDividedSenseList = ListUtils.divideEqually(this.carAgents(), this.commandDivisorMap.get(command));
            final CommandService commandService = this.commandServiceMap.get(command);
            commandService.setup(carDividedSenseList.size());
            final List<Worker> workers = carDividedSenseList.stream().map(car -> (Worker) new WorkerCarBarrier(car)).toList();
            workers.forEach(worker -> worker.setCarCommand(command));
            this.carsWorkersMap.put(command, workers);
        });
    }

    @Override
    public void callNextTaskCommand() {
        final CarCommand command = this.nextCommand();
        final List<Worker> workers = this.carsWorkersMap.get(command);
        System.out.println("\nRUN COMMAND: " + command.getClass().getSimpleName());
//        workers.forEach(worker -> worker.setCarCommand(command));
        this.commandServiceMap.get(command).runTask(this.runTask(workers));
    }

}
