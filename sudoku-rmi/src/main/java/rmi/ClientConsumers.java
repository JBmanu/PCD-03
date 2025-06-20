package rmi;

import grid.Coordinate;

import java.io.Serializable;
import java.util.List;

public final class ClientConsumers {

    public interface CallbackMove extends Serializable {
        void accept(Coordinate coordinate, int value);
    }
    
    public interface CallbackJoinPlayers extends Serializable {
        void accept(String player);
    }
    
    public interface CallbackLeavePlayer extends Serializable {
        void accept(String player);
    }
    
}
