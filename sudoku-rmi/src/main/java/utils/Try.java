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

    static <A, B, R> Optional<R> toOptional(final Dyadic<A, B, R> dyadic, final A arg1, final B arg2) {
        try {
            return Optional.of(dyadic.apply(arg1, arg2));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <A, B, C, R> Optional<R> toOptional(final Triadic<A, B, C, R> triadic,
                                               final A arg1, final B arg2, final C arg3) {
        try {
            return Optional.of(triadic.apply(arg1, arg2, arg3));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    static <A, B, C, D, R> Optional<R> toOptional(final Quadradic<A, B, C, D, R> quadradic,
                                                  final A arg1, final B arg2, final C arg3, final D arg4) {
        try {
            return Optional.of(quadradic.apply(arg1, arg2, arg3, arg4));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    static <A, B, C, D, E, R> Optional<R> toOptional(final Pentadic<A, B, C, D, E, R> pentadic,
                                                     final A arg1, final B arg2, final C arg3,
                                                     final D arg4, final E arg5) {
        try {
            return Optional.of(pentadic.apply(arg1, arg2, arg3, arg4, arg5));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    static <A, B, C, D, E, F, R> Optional<R> toOptional(final Hexadic<A, B, C, D, E, F, R> hexadic,
                                                         final A arg1, final B arg2, final C arg3,
                                                         final D arg4, final E arg5, final F arg6) {
        try {
            return Optional.of(hexadic.apply(arg1, arg2, arg3, arg4, arg5, arg6));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    static <A, B, C, D, E, F, G, R> Optional<R> toOptional(final Heptadic<A, B, C, D, E, F, G, R> heptadic,
                                                            final A arg1, final B arg2, final C arg3,
                                                            final D arg4, final E arg5, final F arg6,
                                                            final G arg7) {
        try {
            return Optional.of(heptadic.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7));
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

    static Optional<Integer> toOptional(final String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (final NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
            return Optional.empty();
        }
    }
}
