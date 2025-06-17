package utils;

import rabbitMQ.RabbitMQConnector;
import rabbitMQ.RabbitMQDiscovery;
import grid.Coordinate;
import grid.Settings;

public final class GameConsumers {

    public interface PlayerData extends Consumers.TriConsumer<String, String, String> {
    }

    public interface PlayerMove extends Consumers.TriConsumer<String, Coordinate, Integer> {
    }

    public interface GridData extends Consumers.QuadConsumer<Settings.Schema, Settings.Difficulty, byte[][], byte[][]> {
    }

    public interface GridRequest extends Consumers.Consumer<String> {
    }

    public interface JoinPlayer extends Consumers.Consumer<String> {
    }

    public interface LeavePlayer extends Consumers.Consumer<String> {
    }

    public interface CallDiscovery extends Consumers.Consumer<RabbitMQDiscovery> {
    }

    public interface CallConnector extends Consumers.Consumer<RabbitMQConnector> {
    }

    public interface CallRabbitMQ extends Consumers.BiConsumer<RabbitMQDiscovery, RabbitMQConnector> {
    }
}


