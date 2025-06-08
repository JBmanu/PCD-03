import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Player;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static model.Player.*;

public class PlayerTest {
    public static final String COUNT_ROOM = "1";
    public static final String COUNT_QUEUE = "1";
    public static final String NAME = "Manu";
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

    private void computePlayer() {
        this.player.computeRoom(COUNT_ROOM);
        this.player.computeQueue(COUNT_ROOM, COUNT_QUEUE, NAME);
        this.player.computeName(NAME);
    }

    @Test
    public void computePlayerData() {
        this.computePlayer();
        final String room = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, COUNT_QUEUE, PLAYER, NAME));

        assertEquals(Optional.of(room), this.player.room());
        assertEquals(Optional.of(queue), this.player.queue());
        assertEquals(Optional.of(NAME), this.player.name());
    }

    @Test
    public void computeRoomID() {
        this.computePlayer();
        final Optional<String> roomID = this.player.computeRoomID();
        assertEquals(Optional.of("room1"), roomID);
    }

    @Test
    public void convertRoomID() {
        this.computePlayer();
        final String expectedRoom = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        final Optional<String> roomID = this.player.computeRoomID();
        final Optional<String> convertedRoom = roomID.map(id -> this.player.convertRoomID(id));
        assertEquals(Optional.of(expectedRoom), convertedRoom);
    }
    
    @Test
    public void computeData() {
        this.player.computeData(COUNT_ROOM, COUNT_QUEUE, NAME);
        final String room = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, COUNT_QUEUE, PLAYER, NAME));

        assertEquals(Optional.of(room), this.player.room());
        assertEquals(Optional.of(queue), this.player.queue());
        assertEquals(Optional.of(NAME), this.player.name());
    }

    @Test
    public void callActionOnData() {
        this.player.callActionOnData((room, queue, name) -> {
            assertEquals(room, this.player.room().orElse(""));
            assertEquals(queue, this.player.queue().orElse(""));
            assertEquals(name, this.player.name().orElse(""));
        });
    }

}
