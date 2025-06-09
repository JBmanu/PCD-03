import grid.Settings;
import model.GridServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Namespace.*;

public class GridServerTest {
    private static final List<Settings> SETTINGS = List.of(
            Settings.create(Settings.Schema.SCHEMA_4x4, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_16X16, Settings.Difficulty.EASY),
            Settings.create(Settings.Schema.SCHEMA_25X25, Settings.Difficulty.EASY));

    private GridServer server;

    @BeforeEach
    public void create() {
        this.server = GridServer.create();
    }

    @Test
    public void createGrid() {
        SETTINGS.forEach(settings -> {
            this.server.createGrid(settings);
            assertTrue(this.server.solution().isPresent());
            assertTrue(this.server.grid().isPresent());
        });
    }
    
    @Test
    public void queueName() {
        final String countRoom = "1";
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom, QUEUE, SERVER));

        SETTINGS.forEach(settings -> {
            this.server.createGrid(settings);
//            final Optional<String> queueName = this.server.computeData(countRoom);
//            assertTrue(queueName.isPresent());
//            assertEquals(Optional.of(queue), queueName);
        });
    }

}
