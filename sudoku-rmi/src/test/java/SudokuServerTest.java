import grid.Coordinate;
import grid.FactoryGrid;
import grid.Settings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmi.*;
import rmi.CallbackServer.CallbackGrid;
import rmi.CallbackServer.CallbackJoinPlayers;
import utils.GridUtils;
import utils.Try;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuServerTest {
    public static final Settings SETTINGS = FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY);

    public static final CallbackClient.CallbackMove IDENTITY_CLIENT_MOVE = (_, _) -> {
    };
    public static final CallbackClient.CallbackJoinPlayers IDENTITY_CLIENT_JOIN_PLAYERS = _ -> {
    };
    public static final CallbackClient.CallbackLeavePlayer IDENTITY_CLIENT_LEAVE_PLAYER = _ -> {
    };

    public static final CallbackGrid IDENTITY_UPDATE_GRID = (_, _) -> {
    };
    public static final CallbackJoinPlayers IDENTITY_JOIN_PLAYERS = _ -> {
    };

    private SudokuServer server;

    @BeforeEach
    public void setup() {
        Try.toOptional(FactoryRMI::createAndRegisterServer);
        final Optional<SudokuServer> serverOptional = FactoryRMI.retrieveServer();
        assertTrue(serverOptional.isPresent(), "Failed to create SudokuServer");
        this.server = serverOptional.get();
    }

    @AfterAll
    public static void shutdown() {
        try {
            FactoryRMI.shutdownServer();
        } catch (final RemoteException | NotBoundException e) {
            fail("Failed to shutdown server: " + e.getMessage());
        }
        Try.toOptional(FactoryRMI::shutdownClient, "manu", 0);
        Try.toOptional(FactoryRMI::shutdownClient, "lu", 0);
    }

    private SudokuClient createRoomWithPlayer(final String name,
                                              final CallbackClient.CallbackMove callbackMove,
                                              final CallbackClient.CallbackJoinPlayers callbackPlayers,
                                              final CallbackClient.CallbackLeavePlayer callbackLeavePlayer,
                                              final CallbackGrid callback) {
        final Optional<SudokuClient> client = Try.toOptional(FactoryRMI::createClient, name, callbackMove, callbackPlayers, callbackLeavePlayer);
        assertTrue(client.isPresent());

        final SudokuClient sudokuClient = client.get();
        Try.toOptional(this.server::createRoom, sudokuClient, SETTINGS, callback);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient);
        return sudokuClient;
    }

    private SudokuClient joinPlayer2(final String name, final SudokuClient client,
                                     final CallbackClient.CallbackMove callbackMove,
                                     final CallbackClient.CallbackJoinPlayers callbackPlayers,
                                     final CallbackClient.CallbackLeavePlayer callbackLeavePlayer,
                                     final CallbackGrid callbackGrid, final CallbackServer.CallbackJoinPlayers callbackJoinPlayers) {
        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final Optional<SudokuClient> client1 = Try.toOptional(FactoryRMI::createClient, name, callbackMove, callbackPlayers, callbackLeavePlayer);
        assertTrue(client1.isPresent());

        final SudokuClient sudokuClient1 = client1.get();
        Try.toOptional(sudokuClient1::setRoomId, roomId);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient1);
        Try.toOptional(this.server::joinRoom, sudokuClient1, callbackGrid, callbackJoinPlayers);
        return sudokuClient1;
    }

    @Test
    public void createRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu",
                IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                (solution, cells) -> {
                    assertNotNull(solution);
                    assertNotNull(cells);
                });
        assertNotNull(client);
    }

    @Test
    public void joinRoom() {
        final String nameClient = "manu";
        final String nameClient1 = "lu";
        final SudokuClient client = this.createRoomWithPlayer(nameClient,
                IDENTITY_CLIENT_MOVE,
                player -> assertEquals(nameClient1, player),
                IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);

        final SudokuClient client1 = this.joinPlayer2(nameClient1, client,
                IDENTITY_CLIENT_MOVE,
                IDENTITY_CLIENT_JOIN_PLAYERS,
                IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID,
                players -> assertEquals(List.of(nameClient), players));

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
    public void joinRoomWithSameName() {
        final String name = "manu";

        final SudokuClient client = this.createRoomWithPlayer(name,
                IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);

        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final Optional<SudokuClient> client1 = Try.toOptional(FactoryRMI::createClient, name, IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER);
        assertTrue(client1.isPresent());

        final SudokuClient sudokuClient1 = client1.get();
        Try.toOptional(sudokuClient1::setRoomId, roomId);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient1);
        final Optional<Boolean> isJoined = Try.toOptional(this.server::joinRoom, sudokuClient1, IDENTITY_UPDATE_GRID, IDENTITY_JOIN_PLAYERS);
        assertTrue(isJoined.isPresent());
        assertFalse(isJoined.get());
    }

    @Test
    public void leaveRoomWithOnePlayer() {
        final SudokuClient client = this.createRoomWithPlayer("manu",
                IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);
        Try.toOptional(this.server::leaveRoom, client);
        final Optional<byte[][]> solutionClient = Try.toOptional(this.server::solution, client);
        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        assertTrue(solutionClient.isEmpty());
        assertTrue(gridClient.isEmpty());
    }

    @Test
    public void leaveRoomWithMultiplePlayers() {
        final String name = "manu";
        final SudokuClient client = this.createRoomWithPlayer(name,
                IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);
        final SudokuClient client1 = this.joinPlayer2("lu", client,
                IDENTITY_CLIENT_MOVE, IDENTITY_CLIENT_JOIN_PLAYERS,
                player -> assertEquals(name, player),
                IDENTITY_UPDATE_GRID, IDENTITY_JOIN_PLAYERS);

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
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        final SudokuClient client = this.createRoomWithPlayer("manu",
                (coordinate1, value1) -> {
                    assertEquals(coordinate, coordinate1);
                    assertEquals(value, value1);
                }
                , IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);
        Try.toOptional(this.server::updateCell, client, coordinate, value);

        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        final Optional<Integer> valueOpt = gridClient.map(grid -> GridUtils.value(grid, coordinate));
        assertEquals(Optional.of(value), valueOpt);
    }

    @Test
    public void updateCellWithMultiplePlayers() {
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        final CallbackClient.CallbackMove moveCheck = (coordinate1, value1) -> {
            assertEquals(coordinate, coordinate1);
            assertEquals(value, value1);
        };

        final SudokuClient client = this.createRoomWithPlayer("manu", moveCheck,
                IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID);

        this.joinPlayer2("lu", client, moveCheck,
                IDENTITY_CLIENT_JOIN_PLAYERS, IDENTITY_CLIENT_LEAVE_PLAYER,
                IDENTITY_UPDATE_GRID, IDENTITY_JOIN_PLAYERS);

        Try.toOptional(this.server::updateCell, client, coordinate, value);
    }


}
