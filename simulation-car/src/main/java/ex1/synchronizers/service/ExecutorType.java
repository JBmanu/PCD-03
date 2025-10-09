package ex1.synchronizers.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum ExecutorType {
    FIXED("Fixed Executor"),
    SCHEDULED("Scheduled Executor"),
    SINGLE("Single Executor");

    private final String name;

    ExecutorType(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ExecutorService createExecutor(final ExecutorType executorType, final int nThreads) {
        return switch (executorType) {
            case FIXED -> Executors.newFixedThreadPool(nThreads);
            case SCHEDULED -> Executors.newScheduledThreadPool(nThreads);
            case SINGLE -> Executors.newSingleThreadExecutor();
        };
    }
}
