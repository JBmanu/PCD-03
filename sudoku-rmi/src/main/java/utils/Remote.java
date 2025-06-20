package utils;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class Remote {

    public static class ExceptionFunctions {
        
        public interface Niladic<R> {
            R apply() throws RemoteException, NotBoundException, AlreadyBoundException;
        }

        public interface Monadic<A, R> {
            R apply(A a) throws RemoteException, NotBoundException, AlreadyBoundException;
        }

        public interface Dyadic<A, B, R> {
            R apply(A a, B b) throws RemoteException, NotBoundException, AlreadyBoundException;
        }

        public interface Triadic<A, B, C, R> {
            R apply(A a, B b, C c) throws RemoteException, NotBoundException, AlreadyBoundException;
        }
        
        public interface Quadradic<A, B, C, D, R> {
            R apply(A a, B b, C c, D d) throws RemoteException, NotBoundException, AlreadyBoundException;
        }
    }

    public static class ExceptionConsumers {

        public interface Consumer<A> {
            void accept(A a) throws RemoteException, NotBoundException, AlreadyBoundException;
        }

        public interface BiConsumer<A, B> {
            void accept(A a, B b) throws RemoteException, NotBoundException, AlreadyBoundException;
        }
        
        public interface TriConsumer<A, B, C> {
            void accept(A a, B b, C c) throws RemoteException, NotBoundException, AlreadyBoundException;
        }
    }

}
