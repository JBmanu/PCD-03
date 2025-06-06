import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RabbitMQConnectorTest {
    private static final String ROOM_NAME = "room1";
    private static final String PLAYER_NAME = "player1";
    private static final String QUEUE_NAME = ROOM_NAME + ".queue1." + PLAYER_NAME;
    
    private RabbitMQConnector connector;
    private GameRoomQueueDiscovery discovery;

    @BeforeEach
    public void create() {
        await().until(() -> {
            this.connector = RabbitMQConnector.create();
            this.discovery = GameRoomQueueDiscovery.create();
            return true;
        });
        assertNotNull(this.connector);
    }

    @Test
    public void createQueue() {
        this.connector.createPlayer(QUEUE_NAME);
        assertEquals(1, this.discovery.countQueues());
    }
    
    @Test
    public void deleteQueue() {
        this.connector.deletePlayer(QUEUE_NAME);
        assertEquals(0, this.discovery.countQueuesWithName(QUEUE_NAME));
    }
    
    @Test
    public void createRoom() {
        this.connector.createPlayer(QUEUE_NAME);
        this.connector.createRoom(ROOM_NAME, QUEUE_NAME, PLAYER_NAME);

        assertEquals(1, this.discovery.countExchangesWithName(ROOM_NAME));
        assertEquals(1, this.discovery.countQueuesWithName(QUEUE_NAME));
    }
    
    @Test
    public void deleteRoom() {
        this.connector.deleteRoom(ROOM_NAME);
        assertEquals(0, this.discovery.countExchangesWithName(ROOM_NAME));
    }

    @AfterEach
    public void cleanup() {
        this.connector.deletePlayer(QUEUE_NAME);
        this.connector.deleteRoom(ROOM_NAME);
    }

}
