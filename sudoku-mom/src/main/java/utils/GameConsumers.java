package utils;

import grid.Coordinate;

public final class GameConsumers {

    public interface PlayerData extends Consumers.TriConsumer<String, String, String> {
    }

    public interface PlayerAction extends Consumers.TriConsumer<String, Coordinate, Integer> {
    }

    public interface GridData extends Consumers.BiConsumer<byte[][], byte[][]> {
    }

}


