package rmi;

import utils.Try;

import java.awt.*;
import java.io.Serializable;

public interface ClientDatas extends Serializable {

    static ClientDatas serializeDatas(final SudokuClient client) {
        final int roomId = Try.toOptional(client::roomId).orElse(-1);
        final String name = Try.toOptional(client::name).orElse("Unknown");
        final Color color = Try.toOptional(client::color).orElse(Color.black);
        return new ClientDatasImpl(roomId, name, color);
    }

    int roomId();

    String name();

    Color color();

    record ClientDatasImpl(int roomId, String name, Color color) implements ClientDatas {
    }
}
