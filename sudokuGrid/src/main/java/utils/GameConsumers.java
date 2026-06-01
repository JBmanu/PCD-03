package utils;

import grid.Coordinate;
import grid.Settings.Difficulty;
import grid.Settings.Schema;

import java.awt.*;
import java.io.Serializable;

public final class GameConsumers {
    @FunctionalInterface
    public interface PlayerData extends Consumers.TriConsumer<String, String, String> {
    }

    @FunctionalInterface
    public interface JoinPlayer extends Consumers.BiConsumer<String, Color> {
    }

    @FunctionalInterface
    public interface LeavePlayer extends Consumers.Consumer<String> {
    }

    @FunctionalInterface
    public interface PlayerMove extends Consumers.TriConsumer<String, Coordinate, Integer> {
    }

    @FunctionalInterface
    public interface CreationGrid extends Consumers.QuadConsumer<Schema, Difficulty, byte[][], byte[][]> {
    }

    @FunctionalInterface
    public interface GridCells extends Serializable, Consumers.BiConsumer<byte[][], byte[][]> {
    }

    @FunctionalInterface
    public interface GridRequest extends Consumers.Consumer<String> {
    }

    @FunctionalInterface
    public interface FocusGained extends Consumers.TriConsumer<String, Color, Coordinate> {
    }

    @FunctionalInterface
    public interface FocusLost extends Consumers.BiConsumer<String, Coordinate> {
    }
}


