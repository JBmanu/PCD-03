import model.Grid;
import model.Settings;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GridTest {

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
    public void checkGridSize() {
        this.settingsList.forEach(settings -> {
            final Grid grid = Grid.create(settings);
            assertEquals(settings.size(), grid.size());
        });
    }
    
    

}
