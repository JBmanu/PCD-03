import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void create() {
        await().until(() -> {
            this.player = Player.create();
            return true;
        });
        
        assertNotNull(this.player);
    }

    @Test
    public void playerDataBeforeGame() {
        assertEquals(Optional.empty(), this.player.room());
        assertEquals(Optional.empty(), this.player.queue());
        assertEquals(Optional.empty(), this.player.name());
    }
    
    
    

}
