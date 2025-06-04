package utils;

import java.util.Optional;

public final class IntUtils {

    public static Optional<Integer> valueOf(final String text) {
        try {
            return Optional.of(Integer.parseInt(text.trim()));
        } catch (final NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    
}
