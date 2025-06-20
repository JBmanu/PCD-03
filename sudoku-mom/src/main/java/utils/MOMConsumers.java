package utils;

import rabbitMQ.RabbitMQConnector;
import rabbitMQ.RabbitMQDiscovery;

public final class MOMConsumers {

    public interface CallDiscovery extends Consumers.Consumer<RabbitMQDiscovery> {
    }

    public interface CallConnector extends Consumers.Consumer<RabbitMQConnector> {
    }

    public interface CallRabbitMQ extends Consumers.BiConsumer<RabbitMQDiscovery, RabbitMQConnector> {
    }
}
