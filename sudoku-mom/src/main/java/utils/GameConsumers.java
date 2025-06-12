package utils;

import grid.Coordinate;

public final class GameConsumers {

    public interface PlayerData extends Consumers.TriConsumer<String, String, String> {
    }

    public interface PlayerMove extends Consumers.TriConsumer<String, Coordinate, Integer> {
    }
    
    public interface GridData extends Consumers.BiConsumer<byte[][], byte[][]> {
    }

    public interface GridRequest extends Consumers.Consumer<String> {
    }

    public interface JoinPlayer extends Consumers.Consumer<String> {
    }

    public interface LeavePlayer extends Consumers.Consumer<String> {
    }

}


