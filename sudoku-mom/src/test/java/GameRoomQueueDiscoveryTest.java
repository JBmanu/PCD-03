import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameRoomQueueDiscoveryTest {

    @Test
    public void create() {
        final GameRoomQueueDiscovery discovery = GameRoomQueueDiscovery.create();
        assertNotNull(discovery);
    }

}
