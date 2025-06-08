import grid.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Player;
import controller.RabbitMQDiscovery;
import controller.RabbitMQConnector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static controller.RabbitMQDiscovery.COUNT_DEFAULT_EXCHANGE;
import static controller.RabbitMQDiscovery.COUNT_DEFAULT_QUEUE_BINDS;

public class RabbitMQConnectorTest {
    private static final String COUNT_ROOM = "1";
    private static final String COUNT_QUEUE = "1";
    private static final String PLAYER_1_NAME = "manu";

    private RabbitMQDiscovery discovery;
    private RabbitMQConnector connector;
    private Player player1;

    @BeforeEach
    public void create() {
        await().atMost(Duration.ofSeconds(10))
                .until(() -> {
                    this.connector = RabbitMQConnector.create();
                    this.discovery = RabbitMQDiscovery.create();
                    this.player1 = Player.create();
                    this.player1.computeToCreateRoom(COUNT_ROOM, COUNT_QUEUE, PLAYER_1_NAME);
                    return this.connector != null;
                });
        assertNotNull(this.connector);
    }

    @AfterEach
    public void cleanup() {
        this.player1.room().ifPresent(this.connector::deleteRoom);
        this.player1.queue().ifPresent(this.connector::deletePlayerQueue);
    }

    @Test
    public void createQueue() {
        this.player1.queue().ifPresent(this.connector::createPlayerQueue);
        assertEquals(1, this.discovery.countQueues());
    }

    @Test
    public void deleteQueue() {
        this.player1.queue().ifPresent(this.connector::deletePlayerQueue);
        assertTrue(this.player1.queue().isPresent());
        assertEquals(0, this.player1.queue()
                .map(this.discovery::countQueuesWithName).orElse(1));
    }

    @Test
    public void createRoom() {
        this.player1.room().ifPresent(this.connector::createRoom);
        assertEquals(COUNT_DEFAULT_EXCHANGE + 1, this.discovery.countExchanges());
    }

    @Test
    public void deleteRoom() {
        this.player1.room().ifPresent(this.connector::deleteRoom);
        assertEquals(0, this.player1.room()
                .map(this.discovery::countExchangesWithName).orElse(1));
    }

    @Test
    public void joinOnRoom() {
        this.player1.room().ifPresent(this.connector::createRoom);
        this.player1.queue().ifPresent(this.connector::createPlayerQueue);
        this.connector.joinRoom(this.player1);

        assertEquals(1, this.player1.room()
                .map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(COUNT_DEFAULT_QUEUE_BINDS + 1, this.player1.queue()
                .map(this.discovery::countQueueBinds).orElse(0));
    }
    
    @Test
    public void createPlayerAndJoin() {
        this.player1.room().ifPresent(this.connector::createRoom);
        this.connector.createPlayerAndJoin(this.player1);

        assertEquals(1, this.player1.room()
                .map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(COUNT_DEFAULT_QUEUE_BINDS + 1, this.player1.queue()
                .map(this.discovery::countQueueBinds).orElse(0));
    }
    
    @Test
    public void createNPlayerAndJoinInSameRoom() {
        this.player1.room().ifPresent(this.connector::createRoom);
        final List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            final Player player = this.computePlayer2(i + "", "player" + i);
            players.add(player);
            this.connector.createPlayerAndJoin(player);
            assertEquals(i, this.discovery.countQueues());
            assertEquals(i, player.room().map(this.discovery::countExchangeBinds).orElse(0));
        }

        players.stream().map(Player::queue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(this.connector::deletePlayerQueue);
   }

    @Test
    public void createRoomPlayerAndJoin() {
        this.connector.createRoomPlayerAndJoin(this.player1);
        assertEquals(1, this.player1.room().map(this.discovery::countExchangesWithName).orElse(0));
        assertEquals(1, this.player1.queue().map(this.discovery::countQueuesWithName).orElse(0));
        assertEquals(1, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
    }

    private Player computePlayer2(final String countQueue, final String name) {
        final Player player2 = Player.create();
        player2.computeToCreateRoom(COUNT_ROOM, countQueue, name);
        return player2;
    }

    private void createRoomWithTwoPlayers(final Player player2) {
        this.connector.createRoomPlayerAndJoin(this.player1);
//        player2.queue().ifPresent(this.connector::createPlayerQueue);
        this.connector.createPlayerAndJoin(player2);
    }

    @Test
    public void connectTwoPlayers() {
        final Player player2 = this.computePlayer2("2", "lu");
        this.createRoomWithTwoPlayers(player2);

        assertEquals(2, this.discovery.countQueues());
        assertEquals(2, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(2, player2.room().map(this.discovery::countExchangeBinds).orElse(0));

        player2.queue().ifPresent(this.connector::deletePlayerQueue);
    }

    @Test
    public void sendMove() {
        final Player player2 = this.computePlayer2("2", "lu");
        this.createRoomWithTwoPlayers(player2);

        this.connector.sendMove(this.discovery, this.player1, Coordinate.create(0, 0), 1);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);

        assertEquals(0, this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(1));
        assertEquals(1, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        player2.queue().ifPresent(this.connector::deletePlayerQueue);
    }

    @Test
    public void receiveMove() {
        final Coordinate coordinate = Coordinate.create(0, 0);
        final int cellValue = 1;
        final Player player2 = this.computePlayer2("2", "lu");
        this.createRoomWithTwoPlayers(player2);

        this.connector.sendMove(this.discovery, this.player1, coordinate, cellValue);
        player2.queue().ifPresent(queue -> this.connector.receiveMove(queue, (player, position, value) -> {
            assertEquals(this.player1.name(), Optional.of(player));
            assertEquals(coordinate, position);
            assertEquals(cellValue, value);
        }));

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(1) == 0);
        assertEquals(0, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        player2.queue().ifPresent(this.connector::deletePlayerQueue);
    }

}
