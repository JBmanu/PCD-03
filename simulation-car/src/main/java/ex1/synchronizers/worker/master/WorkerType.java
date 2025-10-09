package ex1.synchronizers.worker.master;

public enum WorkerType {
    GENERIC("Generic Worker", MultiWorkerGeneric.class),
    SPECIFIC("Specific Worker", MultiWorkerSpecific.class);

    private final String name;
    private final Class<? extends MasterWorker> workerClass;

    WorkerType(final String name, final Class<? extends MasterWorker> workerClass) {
        this.name = name;
        this.workerClass = workerClass;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends MasterWorker> getWorkerClass() {
        return this.workerClass;
    }


}
