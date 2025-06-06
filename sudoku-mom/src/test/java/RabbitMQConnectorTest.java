import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

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
        await().until(() -> {
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

//    @Test
//    public void sendMove() {
//        
//        this.connector.createRoomWithPlayer(ROOM_NAME, QUEUE_NAME, PLAYER_NAME);
//        this.connector.createPlayerQueue(QUEUE_NAME);
//    }

}
