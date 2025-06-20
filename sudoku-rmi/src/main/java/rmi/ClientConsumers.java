package rmi;

import grid.Coordinate;

import java.io.Serializable;

public final class ClientConsumers {

    public interface CallbackMove extends Serializable {
        void accept(Coordinate coordinate, int value);
    }
}
