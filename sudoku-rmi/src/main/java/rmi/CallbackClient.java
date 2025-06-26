package rmi;

import grid.Coordinate;

import java.io.Serializable;
import java.util.List;

public final class CallbackClient {

    public interface CallbackOnMove extends Serializable {
        void callbackOnMove(Coordinate coordinate, int value);
    }

    public interface CallbackOnJoinPlayer extends Serializable {
        void callbackOnJoinPlayer(String player);
    }
    
    public interface CallbackOnJoin extends Serializable {
        void callbackOnJoin(List<String> players);
    }

    public interface CallbackOnLeavePlayer extends Serializable {
        void callbackOnLeavePlayer(String player);
    }
    
    public interface CallbackOnEnter extends Serializable {
        void callbackOnEnter(byte[][] solution, byte[][] cells);
    }
    
    public interface Callbacks extends Serializable, CallbackOnMove, CallbackOnJoinPlayer, CallbackOnJoin, CallbackOnLeavePlayer, CallbackOnEnter {
        
    }

}
