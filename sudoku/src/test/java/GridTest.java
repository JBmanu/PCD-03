import model.Coordinate;
import model.Grid;
import model.Settings;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    public static final int ZERO_VALUE = 0;
    private final List<Settings> settingsList = List.of(
            Settings.create(Settings.Schema.SCHEMA_4x4, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_16X16, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_25X25, Settings.Difficulty.EASY));


    @Test
    public void createGrid() {
        final Settings settings = Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
        final Grid grid = Grid.create(settings);
        assertNotNull(grid);
    }

    @Test
    public void gridSize() {
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
    public void checkCleanValuesOfGrid() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            assertEquals(settings.maxNumbersToClear(), grid.countEmptyValue());
        });
    }

    @Test
    public void gridEqualsOfSolution() {
        this.settingsList.stream().map(Grid::create)
                .forEach(grid -> assertTrue(grid.isGridCreateFromSolution()));
    }
    
    @Test
    public void setValue() {
        this.settingsList.forEach(settings -> {
            final Coordinate coordinate = Coordinate.create(0, 0);
            final int value = 1;
            
            final Grid grid = Grid.create(settings);
            grid.setValue(coordinate, value);
            assertEquals(value, grid.grid().get(coordinate));
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

}
