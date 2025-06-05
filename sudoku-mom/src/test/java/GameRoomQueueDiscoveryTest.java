import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameRoomQueueDiscoveryTest {

    private GameRoomQueueDiscovery discovery;

    @BeforeEach
    public void create() {
        this.discovery = GameRoomQueueDiscovery.create();
        assertNotNull(this.discovery);
    }
    
    @Test
    public void countDefaultExchanges() {
        assertEquals(GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
    }
    
    @Test
    public void countExchangesWithName() {
        final int count = this.discovery.countExchangesWithName(GameRoomQueueDiscovery.DEFAULT_EXCHANGE_NAME);
        assertEquals(1, count);
    }
    
    @Test
    public void countExchangesContainsName() {
        final int count = this.discovery.countExchangesContains("amq");
        assertEquals(GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE - 1, count);
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

}
