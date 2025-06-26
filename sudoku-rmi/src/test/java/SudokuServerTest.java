import grid.Coordinate;
import grid.FactoryGrid;
import grid.Settings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmi.CallbackClient;
import rmi.CallbackClient.*;
import rmi.FactoryRMI;
import rmi.SudokuClient;
import rmi.SudokuServer;
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

    public static final CallbackOnEnter IDENTITY_ON_ENTER = (_, _) -> {
    };
    public static final CallbackOnJoin IDENTITY_ON_JOIN = _ -> {
    };
    public static final CallbackOnMove IDENTITY_ON_MOVE = (_, _) -> {
    };
    public static final CallbackOnJoinPlayer IDENTITY_ON_JOIN_PLAYER = _ -> {
    };
    public static final CallbackLeavePlayer IDENTITY_ON_LEAVE_PLAYER = _ -> {
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
    }

    private SudokuClient createRoomWithPlayer(final String name,
                                              final CallbackOnEnter onEnter,
                                              final CallbackOnJoin onJoin,
                                              final CallbackOnMove onMove,
                                              final CallbackOnJoinPlayer onJoinPlayer,
                                              final CallbackLeavePlayer onLeavePlayer) {
        final Optional<SudokuClient> client = Try.toOptional(FactoryRMI::createClient, name, onEnter, onJoin, onMove, onJoinPlayer, onLeavePlayer);
        assertTrue(client.isPresent());

        final SudokuClient sudokuClient = client.get();
        Try.toOptional(this.server::createRoom, sudokuClient, SETTINGS);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient);
        return sudokuClient;
    }

    private SudokuClient joinPlayer2(final String name, final SudokuClient client,
                                     final CallbackOnEnter onEnter,
                                     final CallbackOnJoin onJoin,
                                     final CallbackOnMove onMove,
                                     final CallbackOnJoinPlayer onJoinPlayer,
                                     final CallbackLeavePlayer onLeavePlayer) {
        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        assertNotEquals(-1, roomId);
        final Optional<SudokuClient> client1 = Try.toOptional(FactoryRMI::createClient, name, onEnter, onJoin, onMove, onJoinPlayer, onLeavePlayer);
        assertTrue(client1.isPresent());

        final SudokuClient sudokuClient1 = client1.get();
        Try.toOptional(sudokuClient1::setRoomId, roomId);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient1);
        final Optional<Boolean> isJoined = Try.toOptional(this.server::joinRoom, sudokuClient1);
        assertEquals(Optional.of(true), isJoined);
        return sudokuClient1;
    }

    @Test
    public void createRoom() {
        final SudokuClient client = this.createRoomWithPlayer("manu",
                (solution, cells) -> {
                    assertNotNull(solution);
                    assertNotNull(cells);
                },
                IDENTITY_ON_JOIN, IDENTITY_ON_MOVE, IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);
        assertNotNull(client);
    }

    @Test
    public void joinRoom() {
        final String nameClient = "manu";
        final String nameClient1 = "lu";
        final SudokuClient client = this.createRoomWithPlayer(nameClient,
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                IDENTITY_ON_MOVE,
                player -> assertEquals(nameClient1, player),
                IDENTITY_ON_LEAVE_PLAYER);

        final SudokuClient client1 = this.joinPlayer2(nameClient1, client,
                IDENTITY_ON_ENTER,
                players -> assertEquals(List.of(nameClient), players),
                IDENTITY_ON_MOVE,
                IDENTITY_ON_JOIN_PLAYER,
                IDENTITY_ON_LEAVE_PLAYER);

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
                IDENTITY_ON_ENTER, IDENTITY_ON_JOIN, IDENTITY_ON_MOVE, IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);

        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final Optional<SudokuClient> client1 = Try.toOptional(FactoryRMI::createClient, name,
                IDENTITY_ON_ENTER, IDENTITY_ON_JOIN, IDENTITY_ON_MOVE, IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);
        assertTrue(client1.isPresent());

        final SudokuClient sudokuClient1 = client1.get();
        Try.toOptional(sudokuClient1::setRoomId, roomId);
        Try.toOptional(FactoryRMI::registerClient, sudokuClient1);
        final Optional<Boolean> isJoined = Try.toOptional(this.server::joinRoom, sudokuClient1);
        assertTrue(isJoined.isPresent());
        assertFalse(isJoined.get());
    }

    @Test
    public void leaveRoomWithOnePlayer() {
        final SudokuClient client = this.createRoomWithPlayer("manu",
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                IDENTITY_ON_MOVE,
                IDENTITY_ON_JOIN_PLAYER,
                IDENTITY_ON_LEAVE_PLAYER);
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
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                IDENTITY_ON_MOVE, IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);
        final SudokuClient client1 = this.joinPlayer2("lu", client,
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                IDENTITY_ON_MOVE,
                IDENTITY_ON_JOIN_PLAYER,
                player -> assertEquals(name, player));

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
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                (coordinate1, value1) -> {
                    assertEquals(coordinate, coordinate1);
                    assertEquals(value, value1);
                }
                , IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);
        Try.toOptional(this.server::updateCell, client, coordinate, value);

        final Optional<byte[][]> gridClient = Try.toOptional(this.server::grid, client);
        final Optional<Integer> valueOpt = gridClient.map(grid -> GridUtils.value(grid, coordinate));
        assertEquals(Optional.of(value), valueOpt);
    }

    @Test
    public void updateCellWithMultiplePlayers() {
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 5;

        final CallbackClient.CallbackOnMove moveCheck = (coordinate1, value1) -> {
            assertEquals(coordinate, coordinate1);
            assertEquals(value, value1);
        };

        final SudokuClient client = this.createRoomWithPlayer("manu",
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN, moveCheck,
                IDENTITY_ON_JOIN_PLAYER,
                IDENTITY_ON_LEAVE_PLAYER);

        this.joinPlayer2("lu", client,
                IDENTITY_ON_ENTER,
                IDENTITY_ON_JOIN,
                moveCheck,
                IDENTITY_ON_JOIN_PLAYER, IDENTITY_ON_LEAVE_PLAYER);

        Try.toOptional(this.server::updateCell, client, coordinate, value);
    }


}
