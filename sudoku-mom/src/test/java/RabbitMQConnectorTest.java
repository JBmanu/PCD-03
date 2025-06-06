import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static rabbitMQ.GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE;

public class RabbitMQConnectorTest {
    private static final String ROOM_NAME = "room1";
    private static final String PLAYER_NAME = "player1";
    private static final String QUEUE_NAME = ROOM_NAME + ".queue1." + PLAYER_NAME;
    
    private GameRoomQueueDiscovery discovery;
    private RabbitMQConnector connector;

    @BeforeEach
    public void create() {
        await().until(() -> {
            this.connector = RabbitMQConnector.create();
            this.discovery = GameRoomQueueDiscovery.create();
            return true;
        });
        assertNotNull(this.connector);
    }

    @AfterEach
    public void cleanup() {
        this.connector.deletePlayerQueue(QUEUE_NAME);
        this.connector.deleteRoom(ROOM_NAME);
    }

    @Test
    public void createQueue() {
        this.connector.createPlayerQueue(QUEUE_NAME);
        assertEquals(1, this.discovery.countQueues());
    }
    
    @Test
    public void deleteQueue() {
        this.connector.deletePlayerQueue(QUEUE_NAME);
        assertEquals(0, this.discovery.countQueuesWithName(QUEUE_NAME));
    }
    
    @Test
    public void createRoom() {
        this.connector.createRoom(ROOM_NAME);
        assertEquals(COUNT_DEFAULT_EXCHANGE + 1, this.discovery.countExchanges());
    }
    
    @Test
    public void deleteRoom() {
        this.connector.deleteRoom(ROOM_NAME);
        assertEquals(0, this.discovery.countExchangesWithName(ROOM_NAME));
    }

    @Test
    public void createRoomWithPlayer() {
        this.connector.createPlayerQueue(QUEUE_NAME);
        this.connector.createRoomWithPlayer(ROOM_NAME, QUEUE_NAME, PLAYER_NAME);

        assertEquals(1, this.discovery.countExchangesWithName(ROOM_NAME));
        assertEquals(1, this.discovery.countQueuesWithName(QUEUE_NAME));
    }

    @Test
    public void joinRoom() {
        this.connector.createRoom(ROOM_NAME);
        this.connector.createPlayerQueue(QUEUE_NAME);
        this.connector.joinRoom(ROOM_NAME, QUEUE_NAME, PLAYER_NAME);
        
        assertEquals(1, this.discovery.countExchangeBinds(ROOM_NAME));
    }


}
