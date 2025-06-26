import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    public static final int ZERO_VALUE = 0;
    private final List<Settings> settingsList = List.of(
            FactoryGrid.settings(Settings.Schema.SCHEMA_4x4, Settings.Difficulty.EASY),
            FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY),
            FactoryGrid.settings(Settings.Schema.SCHEMA_16X16, Settings.Difficulty.EASY),
            FactoryGrid.settings(Settings.Schema.SCHEMA_25X25, Settings.Difficulty.EASY));


    @Test
    public void createCells() {
        final Settings settings = FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
        final Grid grid = FactoryGrid.grid(settings);
        assertNotNull(grid);
    }

    @Test
    public void cellsSize() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            assertEquals(settings.size(), grid.size());
        });
    }

    @Test
    public void isCompleteSolution() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            grid.solution().values().forEach(value ->
                    assertTrue(value > ZERO_VALUE && value <= grid.size()));
        });
    }

    @Test
    public void isValidSolution() {
        this.settingsList.stream()
                .map(FactoryGrid::grid)
                .forEach(grid -> assertTrue(grid.isValidSolution()));
    }

    @Test
    public void checkCleanValuesOfCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void cellsEqualsOfSolution() {
        this.settingsList.stream().map(FactoryGrid::grid)
                .forEach(grid -> assertTrue(grid.isGridCreateFromSolution()));
    }

    @Test
    public void saveValue() {
        this.settingsList.forEach(settings -> {
            final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
            final int value = 1;

            final Grid grid = FactoryGrid.grid(settings);
            grid.saveValue(coordinate, value);
            assertEquals(value, grid.cells().get(coordinate));
        });
    }

    @Test
    public void resetValue() {
        this.settingsList.forEach(settings -> {
            final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
            final int value = 1;

            final Grid grid = FactoryGrid.grid(settings);
            grid.saveValue(coordinate, value);
            assertEquals(value, grid.cells().get(coordinate));
            grid.resetValue(coordinate);
            assertEquals(grid.emptyValue(), grid.cells().get(coordinate));
        });
    }

    @Test
    public void suggest() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
            final Optional<Map.Entry<Coordinate, Integer>> suggest = grid.suggest();
            assertTrue(suggest.isPresent());
            assertNotEquals(grid.emptyValue(), suggest.get().getValue());
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue() + 1);
        });
    }

    @Test
    public void hasWin() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            grid.emptyCells().forEach(_ -> grid.suggest());
            assertEquals(ZERO_VALUE, grid.countEmptyValue());
            assertTrue(grid.hasWin());
        });
    }

    @Test
    public void hasDraw() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            grid.suggest();
            assertNotEquals(ZERO_VALUE, grid.countEmptyValue());
            assertFalse(grid.hasWin());
        });
    }

    @Test
    public void emptyUndo() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final Optional<Coordinate> undoCoordinate = grid.undo();
            assertFalse(grid.canUndo());
            assertTrue(undoCoordinate.isEmpty());
        });
    }

    @Test
    public void undoFromSaveValue() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final Map.Entry<Coordinate, Integer> firstEmptyCell = grid.emptyCells().getFirst();
            final Coordinate coordinate = firstEmptyCell.getKey();
            final int newValue = 1;

            grid.saveValue(firstEmptyCell.getKey(), newValue);
            assertEquals(newValue, grid.valueFrom(coordinate));
            assertTrue(grid.canUndo());
            final Optional<Coordinate> undoCoordinate = grid.undo();
            assertFalse(grid.canUndo());
            assertEquals(Optional.of(coordinate), undoCoordinate);
            assertEquals(grid.emptyValue(), grid.valueFrom(coordinate));
        });
    }

    @Test
    public void undoFromSuggest() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            grid.suggest();
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue() + 1);
            assertTrue(grid.canUndo());
            grid.undo();
            assertFalse(grid.canUndo());
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void resetCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            grid.emptyCells().forEach(_ -> grid.suggest());
            assertTrue(grid.hasWin());
            final Map<Coordinate, Integer> resetCells = grid.reset();
            final long totalEmptyCells = resetCells.values().stream().filter(value -> value == grid.emptyValue()).count();
            assertEquals(settings.maxNumbersToClear(), totalEmptyCells);
            assertEquals(totalEmptyCells, grid.countEmptyValue());
        });
    }

    @Test
    public void orderedCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final List<Coordinate> correctCoordinates = new ArrayList<>();
            final List<Coordinate> currentCoordinates = grid.orderedCells().stream().map(Map.Entry::getKey).toList();

            for (int row = 0; row < settings.size(); row++)
                for (int col = 0; col < settings.size(); col++)
                    correctCoordinates.add(FactoryGrid.coordinate(row, col));

            IntStream.range(0, settings.size() * settings.size())
                    .forEach(index ->
                            assertEquals(correctCoordinates.get(index), currentCoordinates.get(index)));
        });
    }

    @Test
    public void solutionArray() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final Map<Coordinate, Integer> solution = grid.solution();
            final byte[][] solutionArray = grid.solutionArray();

            solution.forEach((coordinate, count) ->
                    assertEquals(count, solutionArray[coordinate.row()][coordinate.col()]));
        });
    }

    @Test
    public void cellsArray() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final Map<Coordinate, Integer> solution = grid.cells();
            final byte[][] cellsArray = grid.cellsArray();

            solution.forEach((coordinate, count) ->
                    assertEquals(count, cellsArray[coordinate.row()][coordinate.col()]));
        });
    }

    private byte[][] arrayEmpty(final Grid grid) {
        final int size = grid.size();
        final byte[][] array = new byte[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                array[row][col] = (byte) grid.emptyValue();
            }
        }
        return array;
    }

    @Test
    public void loadSolution() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final byte[][] solutionArray = this.arrayEmpty(grid);
            grid.loadSolution(solutionArray);

            grid.solution().forEach((coordinate, value) -> {
                assertEquals(grid.emptyValue(), value);
                assertEquals(grid.emptyValue(), grid.solutionArray()[coordinate.row()][coordinate.col()]);
            });
        });
    }

    @Test
    public void loadCells() {
        this.settingsList.forEach(settings -> {
            final Grid grid = FactoryGrid.grid(settings);
            final byte[][] cellsArray = this.arrayEmpty(grid);
            grid.loadCells(cellsArray);

            grid.cells().forEach((coordinate, value) -> {
                assertEquals(grid.emptyValue(), value);
                assertEquals(grid.emptyValue(), grid.cellsArray()[coordinate.row()][coordinate.col()]);
            });
        });
    }

}
