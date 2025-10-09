package ex1.synchronizers.service;

import java.util.List;
import java.util.concurrent.Future;

public interface CommandService {
    void setup(final int total);
    void runTask(final List<? extends Future<?>> futures);
}