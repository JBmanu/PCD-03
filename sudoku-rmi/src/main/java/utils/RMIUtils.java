package utils;

import rmi.SudokuClient;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface RMIUtils {

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

}
