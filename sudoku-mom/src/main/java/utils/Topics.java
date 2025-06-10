package utils;

import model.GridServer;
import model.Player;

import java.util.List;
import java.util.Optional;

public final class Topics {
    public static final String DIVISOR = ".";

    public static final String DOMAIN = "sudoku";
    public static final String ROOM = "room";
    public static final String QUEUE = "queue";

    public static final String SERVER = "server";
    public static final String PLAYER = "player";
    
    public static final String REQUEST = "request";
    public static final String REQUEST_GRID = "grid";


    public static String computeRoomName(final String countRoom) {
        return String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom));
    }

    public static String computePlayerQueueName(final String countRoom, final String countQueue, final String playerName) {
        return String.join(DIVISOR,
                List.of(computeRoomName(countRoom), QUEUE, countQueue, PLAYER, playerName));
    }

    public static String computePlayerQueueNameFrom(final String roomId, final String countQueue, final String playerName) {
        return String.join(DIVISOR,
                List.of(computeRoomNameFrom(roomId), QUEUE, countQueue, PLAYER, playerName));
    }

    public static String computeServerQueueName(final String countRoom) {
        return String.join(DIVISOR, List.of(computeRoomName(countRoom), QUEUE, SERVER));
    }

    public static Optional<String> computeRoomIDFrom(final Player player) {
        return player.room().map(name -> name.replaceAll(DOMAIN + "\\" + DIVISOR, "")
                .replaceAll("\\" + DIVISOR, ""));
    }

    public static Optional<String> computeRoomIDFrom(final GridServer server) {
        return server.room().map(name -> name.replaceAll(DOMAIN + "\\" + DIVISOR, "")
                .replaceAll("\\" + DIVISOR, ""));
    }

    public static String computeRoomNameFrom(final String roomId) {
        return String.join(DIVISOR,
                List.of(DOMAIN, ROOM, roomId.replaceAll(ROOM, "")));
    }
}
