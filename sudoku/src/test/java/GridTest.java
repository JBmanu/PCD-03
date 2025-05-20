import model.Grid;
import model.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GridTest {
    
    @Test
    public void createGrid() {
        final Settings settings = Settings.create(Settings.Size.SCHEMA_9x9, Settings.Difficulty.EASY);
        final Grid grid = Grid.create(settings);
        assertNotNull(grid);
    }
    
    
}
