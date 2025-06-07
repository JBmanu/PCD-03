package utils;

public class Consumers {

    public interface Consumer<T> {
        void accept(T t);
    }

    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
    
}
