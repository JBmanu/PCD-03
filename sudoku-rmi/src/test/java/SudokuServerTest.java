import grid.Coordinate;
import grid.Grid;
import grid.Settings;
import rmi.SudokuClient;
import rmi.SudokuServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuServerTest {
    public static final Settings SETTINGS = Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
    private SudokuServer server;

    @BeforeEach
    public void setup() {
        final Optional<SudokuServer> serverOptional = SudokuServer.create();
        assertTrue(serverOptional.isPresent());
        this.server = serverOptional.get();
    }

    private SudokuClient createRoomWithPlayer(final String name) {
        try {
            final Optional<SudokuClient> clientOptional = this.server.createRoom(name, SETTINGS);
            assertTrue(clientOptional.isPresent());
            return clientOptional.get();
        } catch (final RemoteException e) {
            fail("Failed to create room: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private SudokuClient createPlayer2WithRoomOf(final String name, final SudokuClient client1) {
        final Optional<SudokuClient> newClientOptional = SudokuClient.create(name, client1.roomId());
        assertTrue(newClientOptional.isPresent());
        return newClientOptional.get();
    }

    @Test
    public void createRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        try {
            final Optional<Grid> grid = this.server.grid(client);
            assertTrue(grid.isPresent());
        } catch (final RemoteException e) {
            fail("Failed to retrieve grid: " + e.getMessage());
        }
    }

    @Test
    public void joinRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final SudokuClient client2 = this.createPlayer2WithRoomOf("lu", client);

        try {
            final Optional<Grid> currentGrid = this.server.joinRoom(client2);
            final Optional<Grid> gridFromClient = this.server.grid(client);
            final Optional<Grid> sendGrid = this.server.grid(client2);
            assertTrue(currentGrid.isPresent());
            assertEquals(currentGrid, gridFromClient);
            assertEquals(currentGrid, sendGrid);
        } catch (final RemoteException e) {
            fail("Failed to join room: " + e.getMessage());
        }
    }

    @Test
    public void leaveRoomWithOnePlayer() {
        try {
            final SudokuClient client = this.createRoomWithPlayer("manu");
            this.server.leaveRoom(client);
            assertTrue(this.server.grid(client).isEmpty());
        } catch (final RemoteException e) {
            fail("Failed to leave room: " + e.getMessage());
        }
    }

    @Test
    public void leaveRoomWithMultiplePlayers() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final SudokuClient client2 = this.createPlayer2WithRoomOf("lu", client);

        try {
            this.server.joinRoom(client2);
            this.server.leaveRoom(client);
            assertTrue(this.server.grid(client).isEmpty());
            assertTrue(this.server.grid(client2).isPresent());
        } catch (final RemoteException e) {
            fail("Failed to leave room with multiple players: " + e.getMessage());
        }
    }

    @Test
    public void updateCell() {
        final SudokuClient client = this.createRoomWithPlayer("manu");
        final Coordinate coordinate = Coordinate.create(0, 0);
        final int value = 5;

        try {
            final Optional<Grid> currentGrid = this.server.updateCell(client, coordinate, value);
            final Optional<Grid> grid = this.server.grid(client);
            assertTrue(grid.isPresent());
            assertEquals(currentGrid, grid);
            assertEquals(value, grid.get().valueFrom(coordinate));
        } catch (final RemoteException e) {
            fail("Failed to leave room: " + e.getMessage());
        }
    }

}
