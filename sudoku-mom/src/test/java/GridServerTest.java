import grid.Coordinate;
import grid.Settings;
import model.GridServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Namespace;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void createGameData() {
        final String countRoom = "1";
        final String countQueue = "1";

        SETTINGS.forEach(settings -> {
            this.server.createGrid(settings);
            this.server.createGameData(countRoom, countQueue);

            assertEquals(Optional.of(Namespace.computeRoomName(countRoom)), this.server.room());
            assertEquals(Optional.of(Namespace.computeServerQueueName(countQueue)), this.server.queue());
        });
    }

    @Test
    public void updateGrid() {
        final String countRoom = "1";
        final String countQueue = "1";
        final Coordinate coordinate = Coordinate.create(0, 0);
        final int value = 1;
        SETTINGS.forEach(settings -> {
            this.server.createGrid(settings);
            this.server.createGameData(countRoom, countQueue);
            this.server.updateGrid(coordinate, value);
            assertEquals(Optional.of(value), this.server.value(coordinate));
        });
    }

}
