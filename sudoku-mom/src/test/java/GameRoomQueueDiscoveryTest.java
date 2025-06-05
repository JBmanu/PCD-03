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
    public void countExchanges() {
        assertEquals(GameRoomQueueDiscovery.COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
    }
    
    

}
