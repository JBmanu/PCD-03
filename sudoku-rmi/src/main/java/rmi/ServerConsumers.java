package rmi;

import java.io.Serializable;
import java.util.List;

public final class ServerConsumers {

    public interface CallbackGrid extends Serializable {
        void accept(byte[][] solution, byte[][] cells);
    }
    
    public interface CallbackJoinPlayers extends Serializable {
        void accept(List<String> player);
    }
    
}
