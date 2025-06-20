package utils;

import java.rmi.RemoteException;

public final class Remote {

    public static class ExceptionFunctions {
        
        public interface Niladic<R> {
            R apply() throws RemoteException;
        }

        public interface Monadic<A, R> {
            R apply(A a) throws RemoteException;
        }

        public interface Dyadic<A, B, R> {
            R apply(A a, B b) throws RemoteException;
        }

        public interface Triadic<A, B, C, R> {
            R apply(A a, B b, C c) throws RemoteException;
        }
    }

    public static class ExceptionConsumers {

        public interface Consumer<A> {
            void accept(A a) throws RemoteException;
        }

        public interface BiConsumer<A, B> {
            void accept(A a, B b) throws RemoteException;
        }
        
        public interface TriConsumer<A, B, C> {
            void accept(A a, B b, C c) throws RemoteException;
        }
    }

}
