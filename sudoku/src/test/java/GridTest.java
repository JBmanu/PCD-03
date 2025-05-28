import model.Coordinate;
import model.Grid;
import model.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    public static final int ZERO_VALUE = 0;
    private final List<Settings> settingsList = List.of(
            Settings.create(Settings.Schema.SCHEMA_4x4, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_16X16, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_25X25, Settings.Difficulty.EASY));


    @Test
    public void createCells() {
        final Settings settings = Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
        final Grid grid = Grid.create(settings);
        assertNotNull(grid);
    }

    @Test
    public void cellsSize() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            assertEquals(settings.size(), grid.size());
        });
    }

    @Test
    public void isCompleteSolution() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            grid.solution().values().forEach(value ->
                    assertTrue(value > ZERO_VALUE && value <= grid.size()));
        });
    }

    @Test
    public void isValidSolution() {
        this.settingsList.stream()
                .map(Grid::create)
                .forEach(grid -> assertTrue(grid.isValidSolution()));
    }

    @Test
    public void checkCleanValuesOfCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void cellsEqualsOfSolution() {
        this.settingsList.stream().map(Grid::create)
                .forEach(grid -> assertTrue(grid.isGridCreateFromSolution()));
    }

    @Test
    public void saveValue() {
        this.settingsList.forEach(settings -> {
            final Coordinate coordinate = Coordinate.create(0, 0);
            final int value = 1;

            final Grid grid = Grid.create(settings);
            grid.saveValue(coordinate, value);
            assertEquals(value, grid.cells().get(coordinate));
        });
    }

    @Test
    public void suggest() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
            grid.suggest();
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue() + 1);
        });
    }

    @Test
    public void hasWin() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            grid.emptyCells().forEach(_ -> grid.suggest());
            assertTrue(grid.hasWin());
        });
    }

    @Test
    public void undoFromSaveValue() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            final Map.Entry<Coordinate, Integer> firstEmptyCell = grid.emptyCells().getFirst();
            final Coordinate coordinate = firstEmptyCell.getKey();
            final int newValue = 1;

            grid.saveValue(firstEmptyCell.getKey(), newValue);
            assertEquals(newValue, grid.valueFrom(coordinate));
            grid.undo();
            assertEquals(grid.emptyValue(), grid.valueFrom(coordinate));
        });
    }

    @Test
    public void undoFromSuggest() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            grid.suggest();
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue() + 1);
            grid.undo();
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void resetCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            grid.emptyCells().forEach(_ -> grid.suggest());
            assertTrue(grid.hasWin());
            grid.reset();
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void orderedCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            final List<Coordinate> correctCoordinates = new ArrayList<>();
            final List<Coordinate> currentCoordinates = grid.orderedCells().stream().map(Map.Entry::getKey).toList();

            for (int row = 0; row < settings.size(); row++)
                for (int col = 0; col < settings.size(); col++)
                    correctCoordinates.add(Coordinate.create(row, col));

            IntStream.range(0, settings.size() * settings.size())
                    .forEach(index -> 
                            assertEquals(correctCoordinates.get(index), currentCoordinates.get(index)));
        });
    }

}
