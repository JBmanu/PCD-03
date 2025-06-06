import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.GameRoomQueueDiscovery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static rabbitMQ.GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE;
import static rabbitMQ.GameRoomQueueDiscovery.DEFAULT_EXCHANGE_NAME;

public class GameRoomQueueDiscoveryTest {

    private GameRoomQueueDiscovery discovery;

    @BeforeEach
    public void create() {
        this.discovery = GameRoomQueueDiscovery.create();
        assertNotNull(this.discovery);
    }
    
    @Test
    public void countDefaultExchanges() {
        assertEquals(COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
    }
    
    @Test
    public void countExchangesWithName() {
        final int count = this.discovery.countExchangesWithName(DEFAULT_EXCHANGE_NAME);
        assertEquals(1, count);
    }
    
    @Test
    public void countExchangesThatContainsName() {
        final int count = this.discovery.countExchangesThatContains("amq");
        assertEquals(COUNT_DEFAULT_EXCHANGE - 1, count);
    }

    @Test
    public void countQueues() {
        final int count = this.discovery.countQueues();
        assertEquals(0, count);
    }
    
    @Test
    public void countQueuesWithName() {
        final int count = this.discovery.countQueuesWithName("");
        assertEquals(0, count);
    }
    
    @Test
    public void countQueuesThatContainsName() {
        final int count = this.discovery.countQueuesThatContains("amq");
        assertEquals(0, count);
    }

//    @Test
//    public void queuesThatContainsName() {
//        final List<String> names = this.discovery.queuesNameThatContains("amq");
//        assertEquals(COUNT_DEFAULT_EXCHANGE - 1, names.size());
//    }
}
