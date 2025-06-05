import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {
    
    @Test
    public void create() {
        final Player player = Player.create();
        assertNotNull(player);
    }
    
    
}
