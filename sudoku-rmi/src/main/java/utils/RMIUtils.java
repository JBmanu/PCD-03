package utils;

import rmi.SudokuClient;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface RMIUtils {

//    static boolean comparePlayers(final SudokuClient client1, final SudokuClient client2) {
//        final Optional<Integer> roomIdOpt = Try.toOptional(client1::roomId);
//        final Optional<Integer> roomIdOpt2 = Try.toOptional(client2::roomId);
//        if (roomIdOpt.isEmpty() || roomIdOpt2.isEmpty()) return false;
//        final Optional<String> nameOpt = Try.toOptional(client1::name);
//        final Optional<String> nameOpt2 = Try.toOptional(client2::name);
//        if (nameOpt.isEmpty() || nameOpt2.isEmpty()) return false;
//        return roomIdOpt.equals(roomIdOpt2) && nameOpt.equals(nameOpt2);
//    }

    static boolean comparePlayers(final SudokuClient client1, final SudokuClient client2) {
        final Optional<Integer> roomIdOpt = Try.toOptional(client1::roomId);
        final Optional<Integer> roomIdOpt2 = Try.toOptional(client2::roomId);
        final Optional<String> nameOpt = Try.toOptional(client1::name);
        final Optional<String> nameOpt2 = Try.toOptional(client2::name);
        return Objects.equals(roomIdOpt, roomIdOpt2) && Objects.equals(nameOpt, nameOpt2);
    }

    static boolean containsPlayer(final List<SudokuClient> players, final SudokuClient client) {
        return players.stream().anyMatch(player -> comparePlayers(player, client));
    }

    static Color generateColor(final int index) {
        final float hue = (float) ((index * 0.618033988749895) % 1.0);
        final float saturation = 0.75f;
        final float brightness = 0.95f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

}
