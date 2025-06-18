package utils;

import utils.Remote.ExceptionConsumers.Consumer;
import utils.Remote.ExceptionFunctions.BiFunction;
import utils.Remote.ExceptionFunctions.Function;
import utils.Remote.ExceptionFunctions.TriFunction;

import java.util.Optional;

public final class Try {

    public static <T, R> Optional<R> toOptional(final Function<T, R> function, final T arg) {
        try {
            return Optional.of(function.apply(arg));
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <A, B, R> Optional<R> toOptional(final BiFunction<A, B, R> function, final A arg1, final B arg2) {
        try {
            return Optional.of(function.apply(arg1, arg2));
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <A, B, C, R> Optional<R> toOptional(final TriFunction<A, B, C, R> function,
                                                      final A arg1, final B arg2, final C arg3) {
        try {
            return Optional.of(function.apply(arg1, arg2, arg3));
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> void toOptional(final Consumer<T> consumer, final T arg) {
        try {
            consumer.accept(arg);
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            throw new RuntimeException();
        }
    }

}
