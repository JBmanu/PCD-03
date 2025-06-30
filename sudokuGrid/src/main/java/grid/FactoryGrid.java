package grid;

import java.util.Arrays;
import java.util.Optional;

public final class FactoryGrid {

    public static Settings settings(final Settings.Schema schema, final Settings.Difficulty difficulty) {
        return new Settings.SettingsImpl(schema, difficulty);
    }

    public static Coordinate coordinate(final int row, final int column) {
        return new Coordinate.CoordinateImpl(row, column);
    }

    public static Grid grid(final Settings settings) {
        return new Grid.GridImpl(settings);
    }

    public static Grid grid(final Settings.Schema schema, final Settings.Difficulty difficulty) {
        return new Grid.GridImpl(settings(schema, difficulty));
    }

    public static Grid gridAndLoadData(final Settings settings, final byte[][] solution, final byte[][] cells) {
        final Grid grid = grid(settings);
        grid.loadSolution(solution);
        grid.loadCells(cells);
        return grid;
    }

    public static Grid gridLoad(final byte[][] solution, final byte[][] cells) {
        final int size = solution.length;
        final Optional<Settings.Schema> schemaOpt = Arrays.stream(Settings.Schema.values())
                .filter(schema -> schema.size() == size).findFirst();
        final Settings settings = settings(schemaOpt.orElse(Settings.Schema.SCHEMA_9x9), Settings.Difficulty.EASY);
        return gridAndLoadData(settings, solution, cells);
    }
}
