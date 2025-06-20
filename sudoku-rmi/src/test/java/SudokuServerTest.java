import grid.Coordinate;
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

import static org.junit.jupiter.api.Assertions.*;

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

    private SudokuClient joinPlayer2(final String name, final SudokuClient client, final UpdateGrid callback) {
        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final Optional<SudokuClient> client1 = Try.toOptional(this.server::joinRoom, name, roomId, callback);
        assertTrue(client1.isPresent());
        return client1.get();
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
        final SudokuClient client1 = this.joinPlayer2("lu", client, IDENTITY_UPDATE_GRID);
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

    @Test
    public void leaveRoomWithMultiplePlayers() {
        final SudokuClient client = this.createRoomWithPlayer("manu", IDENTITY_UPDATE_GRID);
        final SudokuClient client1 = this.joinPlayer2("lu", client, IDENTITY_UPDATE_GRID);
        Try.toOptional(this.server::leaveRoom, client);

        final Optional<byte[][]> solutionClient = Try.toOptional(this.server::solution, client);
        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);

        final Optional<byte[][]> solutionClient1 = Try.toOptional(this.server::solution, client1);
        final Optional<byte[][]> gridClient1 = Try.toOptional(this.server::grid, client1);

        assertTrue(Stream.of(solutionClient, gridClient).allMatch(Optional::isEmpty));
        assertTrue(Stream.of(solutionClient1, gridClient1).allMatch(Optional::isPresent));
    }

    @Test
    public void updateCellWithOnePlayer() {
        final SudokuClient client = this.createRoomWithPlayer("manu", IDENTITY_UPDATE_GRID);
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        Try.toOptional(client::setCallbackMove, (coordinate1, value1) -> {
            assertEquals(coordinate, coordinate1);
            assertEquals(value, value1);
        });
        Try.toOptional(this.server::updateCell, client, coordinate, value);

        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        final Optional<Integer> valueOpt = gridClient.map(grid -> GridUtils.value(grid, coordinate));
        assertEquals(Optional.of(value), valueOpt);
    }

    @Test
    public void updateCellWithMultiplePlayers() {
        final SudokuClient client = this.createRoomWithPlayer("manu", IDENTITY_UPDATE_GRID);
        final SudokuClient client1 = this.joinPlayer2("lu", client, IDENTITY_UPDATE_GRID);
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        Try.toOptional(client::setCallbackMove, (coordinate1, value1) -> {
            assertEquals(coordinate, coordinate1);
            assertEquals(value, value1);
        });
        Try.toOptional(client1::setCallbackMove, (coordinate1, value1) -> {
            assertEquals(coordinate, coordinate1);
            assertEquals(value, value1);
        });

        Try.toOptional(this.server::updateCell, client, coordinate, value);
    }

}
