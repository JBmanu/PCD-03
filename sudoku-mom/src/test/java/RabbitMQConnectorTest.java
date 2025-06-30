import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.RabbitMQConnector;
import rabbitMQ.RabbitMQDiscovery;
import utils.GameConsumers;
import utils.GridUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static rabbitMQ.RabbitMQDiscovery.COUNT_DEFAULT_EXCHANGE;
import static rabbitMQ.RabbitMQDiscovery.COUNT_DEFAULT_QUEUE_BINDS;

public class RabbitMQConnectorTest {
    private static final int MESSAGE_JOINED = 1;
    private static final String COUNT_ROOM = "1";
    private static final String COUNT_QUEUE = "1";
    private static final String PLAYER_1_NAME = "manu";

    private static final GameConsumers.JoinPlayer IDENTITY_JOIN_PLAYER = _ -> {
    };
    private static final GameConsumers.LeavePlayer IDENTITY_LEAVE_PLAYER = _ -> {
    };
    private static final GameConsumers.PlayerMove IDENTITY_PLAYER_MOVE = (_, _, _) -> {
    };
    private static final GameConsumers.CreationGrid IDENTITY_CREATION_GRID = (_, _, _, _) -> {
    };

    private RabbitMQDiscovery discovery;
    private RabbitMQConnector connector;
    private Player player1;

    @BeforeEach
    public void create() {
        this.player1 = Player.create();
        this.player1.computeToCreateRoom(COUNT_ROOM, COUNT_QUEUE, PLAYER_1_NAME);

        final Optional<RabbitMQDiscovery> discoveryOpt = RabbitMQDiscovery.create();
        final Optional<RabbitMQConnector> connectorOpt = RabbitMQConnector.create();

        await().atMost(Duration.ofSeconds(10)).until(connectorOpt::isPresent);

        assertTrue(discoveryOpt.isPresent());
        assertTrue(connectorOpt.isPresent());

        this.connector = connectorOpt.get();
        this.discovery = discoveryOpt.get();
    }

    @AfterEach
    public void cleanup() {
        this.connector.deleteQueue(this.discovery, this.player1);
        this.connector.deleteRoom(this.player1);
    }

    @Test
    public void createRoom() {
        this.connector.createRoom(this.player1);

        assertEquals(1, this.player1.room().map(this.discovery::countExchangesWithName).orElse(0));
        assertEquals(0, this.discovery.countQueues());
    }

    @Test
    public void deleteRoom() {
        this.connector.createRoomAndJoin(this.player1);
        this.connector.deleteRoom(this.player1);

        assertEquals(0, this.player1.room().map(this.discovery::countExchangesWithName).orElse(1));
    }

    @Test
    public void createRoomAndJoin() {
        this.connector.createRoomAndJoin(this.player1);

        assertEquals(1, this.player1.room().map(this.discovery::countExchangesWithName).orElse(0));
        assertEquals(1, this.player1.queue().map(this.discovery::countQueuesWithName).orElse(0));
        assertEquals(1, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
    }

    @Test
    public void joinRoom() {
        this.connector.createRoom(this.player1);
        this.connector.joinRoom(this.discovery, this.player1);

        assertEquals(1, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(COUNT_DEFAULT_QUEUE_BINDS + 1, this.player1.queue().map(this.discovery::countQueueBinds).orElse(0));
    }

    @Test
    public void onePlayerLeaveRoom() {
        this.connector.createRoomAndJoin(this.player1);
        this.connector.leaveRoom(this.discovery, this.player1);

        assertEquals(COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
        assertEquals(1, this.discovery.countQueues());
    }

    @Test
    public void twoPlayerAndOnePlayerLeaveRoom() {
        final Player player2 = this.computeNewPlayer("2", "lu");
        this.createRoomWithTwoPlayer(player2);
        this.connector.leaveRoom(this.discovery, player2);

        assertEquals(COUNT_DEFAULT_EXCHANGE + 1, this.discovery.countExchanges());
        assertEquals(2, this.discovery.countQueues());
        assertEquals(1, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void listenWhenPlayerLeaveRoom() {
        final String leavePlayerName = "lu";
        final Player player2 = this.computeNewPlayer("2", leavePlayerName);
        this.createRoomWithTwoPlayer(player2);
        this.connector.leaveRoom(this.discovery, player2);

        this.connector.activeCallbackReceiveMessage(this.player1, null, IDENTITY_JOIN_PLAYER,
                playerName -> assertEquals(leavePlayerName, playerName),
                IDENTITY_PLAYER_MOVE, IDENTITY_CREATION_GRID);

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void deleteQueue() {
        this.connector.createRoomAndJoin(this.player1);
        this.connector.deleteQueue(this.discovery, this.player1);

        assertEquals(0, this.player1.queue().map(this.discovery::countQueuesWithName).orElse(1));
        assertEquals(COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
    }

    private Player computeNewPlayer(final String countQueue, final String name) {
        final Player player2 = Player.create();
        player2.computeToCreateRoom(COUNT_ROOM, countQueue, name);
        return player2;
    }

    private void createRoomWithTwoPlayer(final Player player2) {
        this.connector.createRoomAndJoin(this.player1);
        this.connector.joinRoom(this.discovery, player2);
    }

    @Test
    public void connectTwoPlayers() {
        final Player player2 = this.computeNewPlayer("2", "lu");
        this.createRoomWithTwoPlayer(player2);

        assertEquals(2, this.discovery.countQueues());
        assertEquals(2, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(2, player2.room().map(this.discovery::countExchangeBinds).orElse(0));

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void connectTwoPlayersAndListenJoinPlayer2() {
        final String player2Name = "lu";
        final Player player2 = this.computeNewPlayer("2", player2Name);
        this.createRoomWithTwoPlayer(player2);

        this.connector.activeCallbackReceiveMessage(player2, null, name ->
                        assertEquals(this.player1.name(), Optional.of(name)),
                IDENTITY_LEAVE_PLAYER, IDENTITY_PLAYER_MOVE, IDENTITY_CREATION_GRID);

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void createNPlayerAndJoinInSameRoom() {
        this.connector.createRoom(this.player1);
        final List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            final Player player = this.computeNewPlayer(i + "", "player" + i);
            players.add(player);
            this.connector.joinRoom(this.discovery, player);
            assertEquals(i, this.discovery.countQueues());
            assertEquals(i, player.room().map(this.discovery::countExchangeBinds).orElse(0));
        }

        this.connector.deleteRoom(this.player1);
        players.forEach(player -> this.connector.deleteQueue(this.discovery, player));
    }

    @Test
    public void sendMove() {
        final Player player2 = this.computeNewPlayer("2", "lu");
        this.createRoomWithTwoPlayer(player2);

        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int value = 1;
        this.connector.sendMove(this.discovery, this.player1, coordinate, value);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);

        assertEquals(MESSAGE_JOINED + 1, this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(1));
        assertEquals(1, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        this.connector.deleteQueue(this.discovery, player2);
    }

    private RabbitMQConnector createOtherConnector() {
        final Optional<RabbitMQConnector> connector2Opt = RabbitMQConnector.create();
        await().atMost(Duration.ofSeconds(10)).until(connector2Opt::isPresent);
        assertTrue(connector2Opt.isPresent());
        return connector2Opt.get();
    }

    @Test
    public void receiveMove() {
        final Coordinate coordinate = FactoryGrid.coordinate(0, 0);
        final int cellValue = 1;
        final Player player2 = this.computeNewPlayer("2", "lu");
        final RabbitMQConnector connector2 = this.createOtherConnector();

        this.connector.createRoomAndJoin(this.player1);
        this.connector.joinRoom(this.discovery, player2);

        this.connector.activeCallbackReceiveMessage(player2, null,
                IDENTITY_JOIN_PLAYER, IDENTITY_LEAVE_PLAYER,
                (player, position, value) -> {
                    assertEquals(this.player1.name(), Optional.of(player));
                    assertEquals(coordinate, position);
                    assertEquals(cellValue, value);
                }, IDENTITY_CREATION_GRID);

        connector2.activeCallbackReceiveMessage(player2, null,
                IDENTITY_JOIN_PLAYER, IDENTITY_LEAVE_PLAYER,
                (player, position, value) -> {
                    assertEquals(this.player1.name(), Optional.of(player));
                    assertEquals(coordinate, position);
                    assertEquals(cellValue, value);
                }, IDENTITY_CREATION_GRID);


        this.connector.sendMove(this.discovery, this.player1, coordinate, cellValue);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);
        assertEquals(1, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));


        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(1) == 0);
        assertEquals(0, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void sendGridRequest() {
        final Player player2 = this.computeNewPlayer("2", "lu");
        this.createRoomWithTwoPlayer(player2);
        this.connector.sendGridRequest(this.discovery, player2);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);

        assertEquals(MESSAGE_JOINED + 1, this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(0));
        assertEquals(0, player2.queue().map(this.discovery::countMessageOnQueue).orElse(1));

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void receiveRequestAndSendGrid() {
        final Grid grid = FactoryGrid.grid(FactoryGrid.settings(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.MEDIUM));
        final Player player2 = this.computeNewPlayer("2", "lu");
        this.createRoomWithTwoPlayer(player2);

        this.connector.sendGridRequest(this.discovery, player2);
        this.connector.activeCallbackReceiveMessage(this.player1, grid,
                IDENTITY_JOIN_PLAYER, IDENTITY_LEAVE_PLAYER, IDENTITY_PLAYER_MOVE, IDENTITY_CREATION_GRID);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(1) == 0);

        assertEquals(0, this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(1));

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);

        assertEquals(1, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        this.connector.deleteQueue(this.discovery, player2);
    }

    @Test
    public void receiveGrid() {
        final Grid grid = FactoryGrid.grid(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.MEDIUM);
        final Player player2 = this.computeNewPlayer("2", "lu");
        final RabbitMQConnector connector2 = this.createOtherConnector();
        this.createRoomWithTwoPlayer(player2);

        this.connector.activeCallbackReceiveMessage(this.player1, grid,
                IDENTITY_JOIN_PLAYER, IDENTITY_LEAVE_PLAYER, IDENTITY_PLAYER_MOVE, IDENTITY_CREATION_GRID);
        connector2.activeCallbackReceiveMessage(player2, null,
                IDENTITY_JOIN_PLAYER, IDENTITY_LEAVE_PLAYER, IDENTITY_PLAYER_MOVE,
                (_, _, solution, cells) -> {
                    assertNotNull(cells);
                    assertNotNull(solution);
                    assertTrue(GridUtils.compareArrays(grid.solutionArray(), solution));
                    assertTrue(GridUtils.compareArrays(grid.cellsArray(), cells));
                });

        connector2.sendGridRequest(this.discovery, player2);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(1) == 0);

        assertEquals(0, player2.queue().map(this.discovery::countMessageOnQueue).orElse(1));

        this.connector.deleteQueue(this.discovery, player2);
    }


}
