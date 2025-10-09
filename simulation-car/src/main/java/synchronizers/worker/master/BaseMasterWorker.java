package synchronizers.worker.master;

import car.CarAgent;
import car.command.CarCommand;
import car.command.concrete.ActionCommand;
import car.command.concrete.DecideCommand;
import car.command.concrete.SenseCommand;
import synchronizers.monitor.startStop.StartStopMonitor;
import synchronizers.monitor.startStop.StartStopMonitorImpl;
import synchronizers.worker.slave.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BaseMasterWorker implements MasterWorker {
    private final StartStopMonitor starStopMonitorSimulation;
    private final List<CarCommand> carCommands;
    private final List<CarAgent> carAgents;
    private final ExecutorService executor;
    private int indexCommand;

    public BaseMasterWorker(final ExecutorService executor) {
        this.indexCommand = 0;
        this.executor = executor;
        this.carAgents = new ArrayList<>();
        this.starStopMonitorSimulation = new StartStopMonitorImpl();
        this.carCommands = List.of(new SenseCommand(), new DecideCommand(), new ActionCommand());
    }

    public BaseMasterWorker() {
        this(Executors.newScheduledThreadPool(7));
    }

    public void execute(final int dt) {
        this.startStopMonitorSimulation().pause();
        this.resetIndexCommand();
        this.setDtToCarAgents(dt);
        this.callNextTaskCommand();
        this.startStopMonitorSimulation().awaitUntilPlay();
    }

    public List<? extends Future<?>> runTask(final List<Worker> workers) {
        return workers.stream().map(this.executor::submit).toList();
    }

    protected StartStopMonitor startStopMonitorSimulation() {
        return this.starStopMonitorSimulation;
    }
    public void startSimulation() {
        this.startStopMonitorSimulation().play();
    }

    protected List<CarAgent> carAgents() {
        return this.carAgents;
    }
    public void addCarAgent(final CarAgent carAgent) {
        this.carAgents.add(carAgent);
    }
    protected void setDtToCarAgents(final int dt) {
        this.carAgents.forEach(carAgent -> carAgent.setTimeDt(dt));
    }

    protected List<CarCommand> carCommands() {
        return this.carCommands;
    }
    protected CarCommand command(final int index) {
        return this.carCommands.get(index);
    }
    protected CarCommand nextCommand() {
        return this.command(this.indexCommand++);
    }

    protected void resetIndexCommand() {
        this.indexCommand = 0;
    }
    protected int totalCommands() {
        return this.carCommands.size();
    }
    public boolean hasCommands() {
        return this.indexCommand < this.totalCommands();
    }

    public void terminateExecution() {
        this.executor.shutdown();
    }

}
