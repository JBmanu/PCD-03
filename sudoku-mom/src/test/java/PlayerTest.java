import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Player;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Namespace.*;

public class PlayerTest {
    public static final String COUNT_ROOM = "1";
    public static final String COUNT_QUEUE = "1";
    public static final String NAME = "Manu";
    public static final String ROOM_NAME = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
    public static final String QUEUE_NAME = String.join(DIVISOR, List.of(ROOM_NAME, QUEUE, COUNT_QUEUE, PLAYER, NAME));
    
    
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
    public void computeToCreateRoom() {
        final String room = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, COUNT_QUEUE, PLAYER, NAME));
        this.player.computeToCreateRoom(COUNT_ROOM, COUNT_QUEUE, NAME);

        assertEquals(Optional.of(room), this.player.room());
        assertEquals(Optional.of(queue), this.player.queue());
        assertEquals(Optional.of(NAME), this.player.name());
    }

    @Test
    public void computeRoomID() {
        this.player.computeToCreateRoom(COUNT_ROOM, COUNT_QUEUE, NAME);
        final Optional<String> roomID = this.player.computeRoomID();
        assertEquals(Optional.of("room1"), roomID);
    }

    @Test
    public void computeRoomNameFromId() {
        final String roomName = this.player.computeRoomNameFrom("room1");
        assertEquals(ROOM_NAME, roomName);
    }

    @Test
    public void computeToJoinRoom() {
        final String roomId = "room1";
        this.player.computeToJoinRoom(roomId, COUNT_QUEUE, NAME);

        assertEquals(Optional.of(ROOM_NAME), this.player.room());
        assertEquals(Optional.of(QUEUE_NAME), this.player.queue());
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
