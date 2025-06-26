package rmi;

import grid.Coordinate;

import java.io.Serializable;
import java.util.List;

public final class CallbackClient {

    public interface CallbackOnMove extends Serializable {
        void accept(Coordinate coordinate, int value);
    }

    public interface CallbackOnJoinPlayer extends Serializable {
        void accept(String player);
    }
    
    public interface CallbackOnJoin extends Serializable {
        void accept(List<String> players);
    }

    public interface CallbackLeavePlayer extends Serializable {
        void accept(String player);
    }
    
    public interface CallbackOnEnter extends Serializable {
        void accept(byte[][] solution, byte[][] cells);
    }
    
    public interface Callbacks extends Serializable, CallbackOnMove, CallbackOnJoinPlayer, CallbackOnJoin, CallbackLeavePlayer, CallbackOnEnter {
        
    }

}
