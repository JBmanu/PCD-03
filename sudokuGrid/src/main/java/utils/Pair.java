package utils;

public interface Pair<X, Y> {

    X first();

    Y second();

    static <X, Y> Pair<X, Y> of(final X first, final Y second) {
        return new PairImpl<>(first, second);
    }
    
    record PairImpl<X, Y>(X first, Y second) implements Pair<X, Y> {}

}
