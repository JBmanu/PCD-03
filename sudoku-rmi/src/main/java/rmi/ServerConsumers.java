package rmi;

import java.io.Serializable;

public final class ServerConsumers {

    public interface UpdateGrid extends Serializable {
        void accept(byte[][] solution, byte[][] cells);
    }
    
}
