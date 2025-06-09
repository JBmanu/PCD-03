import grid.Settings;
import model.GridServer;
import model.Player;
import org.junit.jupiter.api.Test;
import ui.utils.PanelUtils;
import utils.Namespace;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Namespace.*;

public class NamespaceTest {
    public static final String COUNT_ROOM = "1";
    public static final String COUNT_QUEUE = "1";
    public static final String NAME = "Manu";

    @Test
    public void computeRoomName() {
        final String room = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        assertEquals(room, Namespace.computeRoomName(COUNT_ROOM));
    }
    
    @Test
    public void computePlayerQueueName() {
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, COUNT_QUEUE, PLAYER, NAME));
        assertEquals(queue, Namespace.computePlayerQueueName(COUNT_ROOM, COUNT_QUEUE, NAME));
    }

    @Test
    public void computePlayerQueueNameFrom() {
        final String roomId = ROOM + COUNT_ROOM;
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, COUNT_QUEUE, PLAYER, NAME));
        assertEquals(queue, Namespace.computePlayerQueueNameFrom(roomId, COUNT_QUEUE, NAME));
    }
    
    @Test
    public void computeServerQueueName() {
        final String queue = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM, QUEUE, SERVER));
        assertEquals(queue, Namespace.computeServerQueueName(COUNT_ROOM));
    }
    
    @Test
    public void computeRoomIDFromPlayer() {
        final Player player = Player.create();
        final String roomId = ROOM + COUNT_ROOM;
        player.computeToCreateRoom(COUNT_ROOM, COUNT_QUEUE, PLAYER);
        
        assertEquals(Optional.of(roomId), Namespace.computeRoomIDFrom(player));
    }
    
    @Test
    public void computeRoomIDFromServer() {
        final GridServer server = GridServer.create();
        final String roomId = ROOM + COUNT_ROOM;
        server.createGrid(Settings.create(Settings.Schema.SCHEMA_9x9, Settings.Difficulty.EASY));
        server.createGameData(COUNT_ROOM, COUNT_QUEUE);
        assertEquals(Optional.of(roomId), Namespace.computeRoomIDFrom(server));
    }
    
    @Test
    public void computeRoomNameFrom() {
        final String roomId = ROOM + COUNT_ROOM;
        final String roomName = String.join(DIVISOR, List.of(DOMAIN, ROOM, COUNT_ROOM));
        assertEquals(roomName, Namespace.computeRoomNameFrom(roomId));
        assertEquals(Namespace.computeRoomName(COUNT_ROOM), Namespace.computeRoomNameFrom(roomId));
    }

}
