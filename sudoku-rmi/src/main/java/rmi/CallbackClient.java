package rmi;

import grid.Coordinate;

import java.io.Serializable;
import java.util.List;

public final class CallbackClient {

    public interface CallbackOnFocusGained extends Serializable {
        void callbackOnFocusGained(ClientDatas player, Coordinate coordinate);
    }

    public interface CallbackOnFocusLost extends Serializable {
        void callbackOnFocusLost(ClientDatas player, Coordinate coordinate);
    }

    public interface CallbackOnMoveGrid extends Serializable {
        void callbackOnMove(Coordinate coordinate, int value);
    }

    public interface CallbackOnJoinPlayer extends Serializable {
        void callbackOnJoinNewPlayer(ClientDatas newPlayer);
    }

    public interface CallbackOnJoin extends Serializable {
        void callbackOnJoinRoom(List<ClientDatas> otherPlayers);
    }

    public interface CallbackOnLeavePlayer extends Serializable {
        void callbackOnLeavePlayer(ClientDatas player);
    }

    public interface CallbackOnEnter extends Serializable {
        void callbackOnEnter(byte[][] solution, byte[][] cells);
    }

    public interface Callbacks extends Serializable,
            CallbackOnFocusGained,
            CallbackOnFocusLost,
            CallbackOnMoveGrid,
            CallbackOnJoinPlayer,
            CallbackOnJoin,
            CallbackOnLeavePlayer,
            CallbackOnEnter {

    }

}
