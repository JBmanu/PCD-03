package utils;

import java.rmi.RemoteException;

public final class Remote {
    
    public static class ExceptionFunctions {
        
        public interface Function<A, R> {
            R apply(A a) throws RemoteException;
        }

        public interface BiFunction<A, B, R> {
            R apply(A a, B b) throws RemoteException;
        }

        public interface TriFunction<A, B, C, R> {
            R apply(A a, B b, C c) throws RemoteException;
        }
    }
    
    public static class ExceptionConsumers {
        
        public interface Consumer<A> {
            void accept(A a) throws RemoteException;
        }
        
    }
    
}
