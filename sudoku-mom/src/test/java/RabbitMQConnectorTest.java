import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RabbitMQConnectorTest {

    private RabbitMQConnector connector;
    
    @BeforeEach
    public void create() {
        await().until(() -> {
            this.connector = RabbitMQConnector.create();
            return true;
        });
        assertNotNull(this.connector);
    }
    
    @Test
    public void createRoom() {
        final String roomName = "room1";
        final String queueName = "queue1";
        final String playerName = "player1";
        this.connector.createRoom(roomName, queueName, playerName);
        
        final GameRoomQueueDiscovery gameRoomQueueDiscovery = GameRoomQueueDiscovery.create();
        assertEquals(1, gameRoomQueueDiscovery.countExchangesWithName(roomName));
        assertEquals(1, gameRoomQueueDiscovery.countQueuesWithName(queueName));
    }
    
    
    
}
