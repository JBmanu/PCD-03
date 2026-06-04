package utils;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

public final class RMITypes {

    public static class ThrowingFunctions {
        
        @FunctionalInterface
        public interface Supplier<R> {
            R apply() throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface Function<A, R> {
            R apply(A a) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface BiFunction<A, B, R> {
            R apply(A a, B b) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface TriFunction<A, B, C, R> {
            R apply(A a, B b, C c) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface QuadFunction<A, B, C, D, R> {
            R apply(A a, B b, C c, D d) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }
        
        @FunctionalInterface
        public interface PentaFunction<A, B, C, D, E, R> {
            R apply(A a, B b, C c, D d, E e) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }
        
        @FunctionalInterface
        public interface HexaFunction<A, B, C, D, E, F, R> {
            R apply(A a, B b, C c, D d, E e, F f) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface HeptaFunction<A, B, C, D, E, F, G, R> {
            R apply(A a, B b, C c, D d, E e, F f, G g) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface OctaFunction<A, B, C, D, E, F, G, H, R> {
            R apply(A a, B b, C c, D d, E e, F f, G g, H h) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface NonaFunction<A, B, C, D, E, F, G, H, I, R> {
            R apply(A a, B b, C c, D d, E e, F f, G g, H h, I i) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }
    }

    public static class ThrowingConsumers {

        @FunctionalInterface
        public interface Consumer<A> {
            void accept(A a) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface BiConsumer<A, B> {
            void accept(A a, B b) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }

        @FunctionalInterface
        public interface TriConsumer<A, B, C> {
            void accept(A a, B b, C c) throws java.rmi.RemoteException, NotBoundException, AlreadyBoundException;
        }
    }

}
