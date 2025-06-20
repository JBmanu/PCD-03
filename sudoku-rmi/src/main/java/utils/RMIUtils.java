package utils;

import rmi.SudokuClient;

import java.util.List;
import java.util.Optional;

public final class RMIUtils {

    public static boolean comparePlayers(final SudokuClient client1, final SudokuClient client2) {
        final Optional<Integer> roomIdOpt = Try.toOptional(client1::roomId);
        final Optional<Integer> roomIdOpt2 = Try.toOptional(client2::roomId);
        if (roomIdOpt.isEmpty() || roomIdOpt2.isEmpty()) return false;
        final Optional<String> nameOpt = Try.toOptional(client1::name);
        final Optional<String> nameOpt2 = Try.toOptional(client2::name);
        if (nameOpt.isEmpty() || nameOpt2.isEmpty()) return false;
        return roomIdOpt.equals(roomIdOpt2) && nameOpt.equals(nameOpt2);
    }
    
    public static boolean containsPlayer(final List<SudokuClient> players, final SudokuClient client) {
        return players.stream().anyMatch(player -> comparePlayers(player, client));
    }
    
    
}
