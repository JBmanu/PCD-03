package utils;

import utils.RMITypes.ThrowingConsumers.BiConsumer;
import utils.RMITypes.ThrowingConsumers.Consumer;
import utils.RMITypes.ThrowingConsumers.TriConsumer;
import utils.RMITypes.ThrowingFunctions.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

public interface Try {

    static <R> Optional<R> toOptional(final Supplier<R> supplier) {
        try {
            return Optional.of(supplier.apply());
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <T, R> Optional<R> toOptional(final Function<T, R> function, final T arg) {
        try {
            return Optional.of(function.apply(arg));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <A, B, R> Optional<R> toOptional(final BiFunction<A, B, R> biFunction, final A arg1, final B arg2) {
        try {
            return Optional.of(biFunction.apply(arg1, arg2));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <A, B, C, R> Optional<R> toOptional(final TriFunction<A, B, C, R> triFunction,
                                               final A arg1, final B arg2, final C arg3) {
        try {
            return Optional.of(triFunction.apply(arg1, arg2, arg3));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <A, B, C, D, R> Optional<R> toOptional(final QuadFunction<A, B, C, D, R> quadFunction,
                                                  final A arg1, final B arg2, final C arg3, final D arg4) {
        try {
            return Optional.of(quadFunction.apply(arg1, arg2, arg3, arg4));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }
    
    static <A, B, C, D, E, R> Optional<R> toOptional(final PentaFunction<A, B, C, D, E, R> pentaFunction,
                                                     final A arg1, final B arg2, final C arg3,
                                                     final D arg4, final E arg5) {
        try {
            return Optional.of(pentaFunction.apply(arg1, arg2, arg3, arg4, arg5));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }
    
    static <A, B, C, D, E, F, R> Optional<R> toOptional(final HexaFunction<A, B, C, D, E, F, R> hexaFunction,
                                                         final A arg1, final B arg2, final C arg3,
                                                         final D arg4, final E arg5, final F arg6) {
        try {
            return Optional.of(hexaFunction.apply(arg1, arg2, arg3, arg4, arg5, arg6));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }
    
    static <A, B, C, D, E, F, G, R> Optional<R> toOptional(final HeptaFunction<A, B, C, D, E, F, G, R> heptaFunction,
                                                            final A arg1, final B arg2, final C arg3,
                                                            final D arg4, final E arg5, final F arg6,
                                                            final G arg7) {
        try {
            return Optional.of(heptaFunction.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <A, B, C, D, E, F, G, H, R> Optional<R> toOptional(final OctaFunction<A, B, C, D, E, F, G, H, R> octaFunction,
                                                              final A arg1, final B arg2, final C arg3,
                                                              final D arg4, final E arg5, final F arg6,
                                                              final G arg7,  final H arg8) {
        try {
            return Optional.of(octaFunction.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8));
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();             
            throw new RuntimeException();
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
            e.printStackTrace();             
            throw new RuntimeException();
        }
    }

    static <A, B, C> void toOptional(final TriConsumer<A, B, C> consumer,
                                     final A arg1, final B arg2, final C arg3) {
        try {
            consumer.accept(arg1, arg2, arg3);
        } catch (final RemoteException | NotBoundException | AlreadyBoundException e) {
            System.out.println("Error during remote operation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static Optional<Integer> toOptional(final String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (final NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
