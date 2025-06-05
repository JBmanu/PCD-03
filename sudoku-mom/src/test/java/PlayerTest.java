import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Player;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static player.Player.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void create() {
        this.player = Player.create();
        assertNotNull(this.player);
    }

    @Test
    public void playerData() {
        assertEquals(Optional.empty(), this.player.room());
        assertEquals(Optional.empty(), this.player.queue());
        assertEquals(Optional.empty(), this.player.name());
    }

    @Test
    public void computePlayerData() {
        final String countRoom = "1";
        final String countQueue = "1";
        final String name = "Manu";
        this.player.computeRoom(countRoom);
        this.player.computeQueue(countRoom, countQueue, name);
        this.player.name(name);

        final String room = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom));
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom, QUEUE, countQueue, PLAYER, name));

        assertEquals(Optional.of(room), this.player.room());
        assertEquals(Optional.of(queue), this.player.queue());
        assertEquals(Optional.of(name), this.player.name());
    }


}
