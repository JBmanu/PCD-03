import grid.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static rabbitMQ.GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE;
import static rabbitMQ.GameRoomQueueDiscovery.COUNT_DEFAULT_QUEUE_BINDS;

public class RabbitMQConnectorTest {
    private static final String COUNT_ROOM = "1";
    private static final String COUNT_QUEUE = "1";
    private static final String PLAYER_1_NAME = "manu";

    private GameRoomQueueDiscovery discovery;
    private RabbitMQConnector connector;
    private Player player1;

    @BeforeEach
    public void create() {
        await().atMost(Duration.ofSeconds(10))
                .until(() -> {
                    this.connector = RabbitMQConnector.create();
                    this.discovery = GameRoomQueueDiscovery.create();
                    this.player1 = Player.create();
                    this.player1.computeData(COUNT_ROOM, COUNT_QUEUE, PLAYER_1_NAME);
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
    public void createRoomWithPlayer() {
        this.connector.createRoomWithPlayer(this.player1);
        assertEquals(1, this.player1.room().map(this.discovery::countExchangesWithName).orElse(0));
        assertEquals(1, this.player1.queue().map(this.discovery::countQueuesWithName).orElse(0));
        assertEquals(1, this.player1.room().map(this.discovery::countExchangeBinds).orElse(0));
    }

    @Test
    public void joinRoom() {
        this.player1.room().ifPresent(this.connector::createRoom);
        this.player1.queue().ifPresent(this.connector::createPlayerQueue);
        this.connector.joinRoom(this.player1);

        assertEquals(1, this.player1.room()
                .map(this.discovery::countExchangeBinds).orElse(0));
        assertEquals(COUNT_DEFAULT_QUEUE_BINDS + 1, this.player1.queue()
                .map(this.discovery::countQueueBinds).orElse(0));
    }

    @Test
    public void sendMove() {
        final Player player2 = Player.create();
        player2.computeData(COUNT_ROOM, "2", "lu");

        this.connector.createRoomWithPlayer(this.player1);
        player2.queue().ifPresent(this.connector::createPlayerQueue);
        this.connector.joinRoom(player2);
        assertEquals(2, this.discovery.countQueues());

        this.connector.sendMove(this.discovery, this.player1, Coordinate.create(0, 0), 1);

        await().atMost(Duration.ofSeconds(10))
                .until(() -> player2.queue().map(this.discovery::countMessageOnQueue).orElse(0) > 0);

        assertEquals(0, this.player1.queue().map(this.discovery::countMessageOnQueue).orElse(1));
        assertEquals(1, player2.queue().map(this.discovery::countMessageOnQueue).orElse(0));

        player2.queue().ifPresent(this.connector::deletePlayerQueue);
    }

    @Test
    public void receiveMove() {
        final Player player2 = Player.create();
        player2.computeData(COUNT_ROOM, "2", "lu");

        this.connector.createRoomWithPlayer(this.player1);
        player2.queue().ifPresent(this.connector::createPlayerQueue);
        this.connector.joinRoom(player2);

        this.connector.sendMove(this.discovery, this.player1, Coordinate.create(0, 0), 1);
        player2.queue().ifPresent(queue -> this.connector.receiveMessage(queue, (player, coordinate, value) -> {
//            assertEquals(this.player1.name(), player);
//            assertEquals(Coordinate.create(0, 0), coordinate);
//            assertEquals(1, value);
        }));

//        await().atMost(Duration.ofSeconds(10))
//                .until(() -> )

        player2.queue().ifPresent(this.connector::deletePlayerQueue);
    }

}
