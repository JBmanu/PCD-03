import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmi.FactoryRMI;
import rmi.SudokuClient;
import rmi.SudokuServer;
import utils.Try;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuServerTest {
    public static final Settings SETTINGS = FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
    private SudokuServer server;

    @BeforeEach
    public void setup() {
        FactoryRMI.server();
        final Optional<SudokuServer> serverOptional = FactoryRMI.retrieveServer();
        assertTrue(serverOptional.isPresent(), "Failed to create SudokuServer");
        this.server = serverOptional.get();
    }

    private SudokuClient createRoomWithPlayer(final String name) {
        final Optional<SudokuClient> clientOpt = Try.toOptional(this.server::createRoom, name, SETTINGS);
        assertTrue(clientOpt.isPresent());
        return clientOpt.get();
    }

    private SudokuClient createPlayer2WithRoomOf(final String name, final SudokuClient client1) {
        final Optional<SudokuClient> newClientOptional = FactoryRMI.client(name, client1.roomId());
        assertTrue(newClientOptional.isPresent());
        return newClientOptional.get();
    }

    @Test
    public void createRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final Optional<Grid> grid = Try.toOptional(this.server::grid, client);
        assertTrue(grid.isPresent());
    }

    @Test
    public void joinRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final SudokuClient client2 = this.createPlayer2WithRoomOf("lu", client);
        final Optional<Grid> currentGrid = Try.toOptional(this.server::joinRoom, client2);
        final Optional<Grid> gridFromClient = Try.toOptional(this.server::grid, client);
        final Optional<Grid> sendGrid = Try.toOptional(this.server::grid, client2);
        assertTrue(currentGrid.isPresent());
        assertEquals(currentGrid, gridFromClient);
        assertEquals(currentGrid, sendGrid);
    }

    @Test
    public void leaveRoomWithOnePlayer() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        Try.toOptional(this.server::leaveRoom, client);
        final Optional<Grid> grid = Try.toOptional(this.server::grid, client);
        assertTrue(grid.isEmpty());

    }

    @Test
    public void leaveRoomWithMultiplePlayers() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final SudokuClient client2 = this.createPlayer2WithRoomOf("lu", client);
        Try.toOptional(this.server::joinRoom, client2);
        final Optional<Grid> gridClient = Try.toOptional(this.server::grid, client);
        final Optional<Grid> gridClient2 = Try.toOptional(this.server::grid, client2);
        Try.toOptional(this.server::leaveRoom, client);
        assertTrue(gridClient.isEmpty());
        assertTrue(gridClient2.isPresent());
    }

    @Test
    public void updateCell() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        final Optional<Grid> currentGrid = Try.toOptional(this.server::updateCell, client, coordinate, value);
        final Optional<Grid> grid = Try.toOptional(this.server::grid, client);
        assertTrue(grid.isPresent());
        assertEquals(currentGrid, grid);
        assertEquals(value, grid.get().valueFrom(coordinate));
    }

}
