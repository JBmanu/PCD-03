package utils;

import utils.Remote.ExceptionConsumers.BiConsumer;
import utils.Remote.ExceptionConsumers.Consumer;
import utils.Remote.ExceptionFunctions.Dyadic;
import utils.Remote.ExceptionFunctions.Monadic;
import utils.Remote.ExceptionFunctions.Niladic;
import utils.Remote.ExceptionFunctions.Triadic;

import java.util.Optional;

public final class Try {

    public static <R> Optional<R> toOptional(final Niladic<R> monadic) {
        try {
            return Optional.of(monadic.apply());
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <T, R> Optional<R> toOptional(final Monadic<T, R> monadic, final T arg) {
        try {
            return Optional.of(monadic.apply(arg));
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <A, B, R> Optional<R> toOptional(final Dyadic<A, B, R> function, final A arg1, final B arg2) {
        try {
            return Optional.of(function.apply(arg1, arg2));
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static <A, B, C, R> Optional<R> toOptional(final Triadic<A, B, C, R> function,
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

    public static <A, B> void toOptional(final BiConsumer<A, B> consumer, final A arg1, final B arg2) {
        try {
            consumer.accept(arg1, arg2);
        } catch (final Exception e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
