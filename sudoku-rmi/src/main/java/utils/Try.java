package utils;

import utils.Remote.ExceptionConsumers.BiConsumer;
import utils.Remote.ExceptionConsumers.Consumer;
import utils.Remote.ExceptionConsumers.TriConsumer;
import utils.Remote.ExceptionFunctions.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

public interface Try {

    static <R> Optional<R> toOptional(final Niladic<R> niladic) {
        try {
            return Optional.of(niladic.apply());
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <T, R> Optional<R> toOptional(final Monadic<T, R> monadic, final T arg) {
        try {
            return Optional.of(monadic.apply(arg));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <A, B, R> Optional<R> toOptional(final Dyadic<A, B, R> function, final A arg1, final B arg2) {
        try {
            return Optional.of(function.apply(arg1, arg2));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <A, B, C, R> Optional<R> toOptional(final Triadic<A, B, C, R> function,
                                               final A arg1, final B arg2, final C arg3) {
        try {
            return Optional.of(function.apply(arg1, arg2, arg3));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <A, B, C, D, R> Optional<R> toOptional(final Quadradic<A, B, C, D, R> function,
                                                  final A arg1, final B arg2, final C arg3, final D arg4) {
        try {
            return Optional.of(function.apply(arg1, arg2, arg3, arg4));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <T> void toOptional(final Consumer<T> consumer, final T arg) {
        try {
            consumer.accept(arg);
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    static <A, B> void toOptional(final BiConsumer<A, B> consumer, final A arg1, final B arg2) {
        try {
            consumer.accept(arg1, arg2);
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    static <A, B, C> void toOptional(final TriConsumer<A, B, C> consumer,
                                     final A arg1, final B arg2, final C arg3) {
        try {
            consumer.accept(arg1, arg2, arg3);
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            throw new RuntimeException();
        }
    }

}
