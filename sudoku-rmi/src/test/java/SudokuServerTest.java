import grid.FactoryGrid;
import grid.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmi.FactoryRMI;
import rmi.ServerConsumers.UpdateGrid;
import rmi.SudokuClient;
import rmi.SudokuServer;
import utils.GridUtils;
import utils.Try;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuServerTest {
    public static final Settings SETTINGS = FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);
    public static final UpdateGrid IDENTITY_UPDATE_GRID = (_, _) -> {
    };

    private SudokuServer server;

    @BeforeEach
    public void setup() {
        FactoryRMI.server();
        final Optional<SudokuServer> serverOptional = FactoryRMI.retrieveServer();
        assertTrue(serverOptional.isPresent(), "Failed to create SudokuServer");
        this.server = serverOptional.get();
    }

    private SudokuClient createRoomWithPlayer(final String name, final UpdateGrid callback) {
        final Optional<SudokuClient> clientOpt = Try.toOptional(this.server::createRoom, name, SETTINGS, callback);
        assertTrue(clientOpt.isPresent());
        return clientOpt.get();
    }

    private SudokuClient createPlayer2WithRoomOf(final String name, final SudokuClient client, final UpdateGrid callback) {
        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final Optional<SudokuClient> client1 = Try.toOptional(this.server::joinRoom, name, roomId, callback);
        assertTrue(client1.isPresent());
        return client1.get();
    }

    private void createRoomWith2Players(final String name1, final String name2, final UpdateGrid callback, final UpdateGrid callback1) {
        final SudokuClient client = this.createRoomWithPlayer(name1, callback);
        final SudokuClient client1 = this.createPlayer2WithRoomOf(name2, client, callback1);
    }

    @Test
    public void createRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu", (solution, cells) -> {
            assertNotNull(solution);
            assertNotNull(cells);
        });
        assertNotNull(client);
    }

    @Test
    public void joinRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu", IDENTITY_UPDATE_GRID);
        final SudokuClient client1 = this.createPlayer2WithRoomOf("lu", client, IDENTITY_UPDATE_GRID);
        final Optional<byte[][]> solutionClient = Try.toOptional(this.server::solution, client);
        final Optional<byte[][]> solutionClient1 = Try.toOptional(this.server::solution, client1);
        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        final Optional<byte[][]> gridClient1 = Try.toOptional(this.server::grid, client1);

        assertTrue(Stream.of(solutionClient, solutionClient1, gridClient, gridClient1)
                .allMatch(Optional::isPresent));
        assertTrue(GridUtils.compareArrays(solutionClient.get(), solutionClient1.get()));
        assertTrue(GridUtils.compareArrays(gridClient.get(), gridClient1.get()));
    }

    @Test
    public void leaveRoomWithOnePlayer() {
        final SudokuClient client = this.createRoomWithPlayer("manu", IDENTITY_UPDATE_GRID);
        Try.toOptional(this.server::leaveRoom, client);
        final Optional<byte[][]> solutionClient = Try.toOptional(this.server::solution, client);
        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        assertTrue(solutionClient.isEmpty());
        assertTrue(gridClient.isEmpty());
    }

//    @Test
//    public void leaveRoomWithMultiplePlayers() {
//        final SudokuClient client = this.createRoomWithPlayer("manu");
//        final SudokuClient client2 = this.createPlayer2WithRoomOf("lu", client);
//        Try.toOptional(client1 -> {
//            this.server.joinRoom(client1, );
//        }, client2);
//        final Optional<Grid> gridClient = Try.toOptional(client1 -> {
//            return this.server.grid(client1, );
//        }, client);
//        final Optional<Grid> gridClient2 = Try.toOptional(client1 -> {
//            return this.server.grid(client1, );
//        }, client2);
//        Try.toOptional(this.server::leaveRoom, client);
//        assertTrue(gridClient.isEmpty());
//        assertTrue(gridClient2.isPresent());
//    }

//    @Test
//    public void updateCell() {
//        final SudokuClient client = this.createRoomWithPlayer("manu");
//        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
//        final int value = 5;
//
//        final Optional<Grid> currentGrid = Try.toOptional(this.server::updateCell, client, coordinate, value);
//        final Optional<Grid> grid = Try.toOptional(client1 -> {
//            return this.server.grid(client1, );
//        }, client);
//        assertTrue(grid.isPresent());
//        assertEquals(currentGrid, grid);
//        assertEquals(value, grid.get().valueFrom(coordinate));
//    }

}
