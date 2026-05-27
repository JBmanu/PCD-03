package rmi;

import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;

public interface ClientDatas extends Serializable {
    
    static ClientDatas serializeDatas(final SudokuClient client) throws RemoteException {
        return new ClientDatasImpl(client.roomId(), client.name(), client.color());
    }
    
    int roomId();
    String name();
    Color color();
    
    record ClientDatasImpl(int roomId, String name, Color color) implements ClientDatas {
    }
}
