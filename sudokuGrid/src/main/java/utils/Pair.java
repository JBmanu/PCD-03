package utils;

import java.io.Serial;
import java.io.Serializable;

public interface Pair<X, Y> extends Serializable {

    X first();

    Y second();

    static <X, Y> Pair<X, Y> of(final X first, final Y second) {
        return new PairImpl<>(first, second);
    }
    
    record PairImpl<X, Y>(X first, Y second) implements Pair<X, Y> {
        @Serial
        private static final long serialVersionUID = 1L;
    }

}
