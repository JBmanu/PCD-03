package ex1.synchronizers.monitor.startStop;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StartStopMonitorImpl implements StartStopMonitor {
    private final Lock mutex;
    private final Condition conditionRunning;
    private boolean isAwaiting;

    public StartStopMonitorImpl() {
        this.mutex = new ReentrantLock();
        this.conditionRunning = this.mutex.newCondition();
        this.isAwaiting = false;
    }

    private void setAwaiting(final boolean isAwaiting) {
        try {
            this.mutex.lock();
            this.isAwaiting = isAwaiting;
        } finally {
            this.mutex.unlock();
        }
    }

    @Override
    public void play() {
        try {
            this.mutex.lock();
            this.isAwaiting = false;
            this.conditionRunning.signalAll();
        } finally {
            this.mutex.unlock();
        }
    }

    @Override
    public void pause() {
        try {
            this.mutex.lock();
            this.isAwaiting = true;
        } finally {
            this.mutex.unlock();
        }
    }

    @Override
    public void awaitUntilPlay() {
        try {
            this.mutex.lock();
            while (this.isAwaiting) {
                this.conditionRunning.awaitUninterruptibly();
            }
        } finally {
            this.mutex.unlock();
        }
    }
}
