package rabbitMQ;

import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;
import com.rabbitmq.http.client.domain.BindingInfo;
import com.rabbitmq.http.client.domain.QueueInfo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public interface GameRoomQueueDiscovery {
    String DEFAULT_EXCHANGE_NAME = "";
    int COUNT_DEFAULT_EXCHANGE = 7;
    int COUNT_DEFAULT_QUEUE_BINDS = 1;

    static GameRoomQueueDiscovery create() {
        return new GameRoomQueueDiscoveryImpl();
    }

    int countExchanges();

    int countExchangesWithoutDefault();

    int countExchangesWithName(String name);

    int countExchangesThatContains(String subString);

    int countQueues();

    int countQueuesWithName(String name);

    int countQueuesThatContains(String subString);

    int countExchangeBinds(String roomName);

    int countQueueBinds(String queueName);

    List<String> routingKeysFromBindsExchange(String roomName);

    int countMessageOnQueue(String queueName);


    class GameRoomQueueDiscoveryImpl implements GameRoomQueueDiscovery {
        private static final String HOST = "kangaroo.rmq.cloudamqp.com";
        private static final String USERNAME = "fanltles";
        private static final String PASSWORD = "6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0";
        private static final String URL = "https://" + HOST + "/api/";

        private final Client client;

        public GameRoomQueueDiscoveryImpl() {
            try {
                this.client = new Client(new ClientParameters()
                        .url(URL)
                        .username(USERNAME)
                        .password(PASSWORD));
            } catch (final URISyntaxException | MalformedURLException e) {
                throw new RuntimeException("Failed to create RabbitMQ HTTP client", e);
            }
        }

        @Override
        public int countExchanges() {
            return this.client.getExchanges().size();
        }

        @Override
        public int countExchangesWithoutDefault() {
            return this.countExchanges() - COUNT_DEFAULT_EXCHANGE;
        }

        @Override
        public int countExchangesWithName(final String name) {
            return (int) this.client.getExchanges().stream()
                    .filter(exchangeInfo -> exchangeInfo.getName().equals(name))
                    .count();
        }

        @Override
        public int countExchangesThatContains(final String subString) {
            return (int) this.client.getExchanges().stream()
                    .filter(exchange -> exchange.getName().contains(subString))
                    .count();
        }

        @Override
        public int countQueues() {
            return this.client.getQueues().size();
        }

        @Override
        public int countQueuesWithName(final String name) {
            return (int) this.client.getQueues().stream()
                    .filter(queueInfo -> queueInfo.getName().equals(name))
                    .count();
        }

        @Override
        public int countQueuesThatContains(final String subString) {
            return (int) this.client.getQueues().stream()
                    .filter(queue -> queue.getName().contains(subString))
                    .count();
        }

        @Override
        public int countExchangeBinds(final String roomName) {
            return this.client.getBindingsBySource(USERNAME, roomName).size();
        }

        @Override
        public int countQueueBinds(final String queueName) {
            return this.client.getQueues(USERNAME).stream()
                    .filter(queue -> queue.getName().equals(queueName))
                    .findFirst()
                    .map(queueInfo -> this.client.getQueueBindings(USERNAME, queueInfo.getName()).size())
                    .orElse(0);
        }

        @Override
        public List<String> routingKeysFromBindsExchange(final String roomName) {
            return this.client.getBindingsBySource(USERNAME, roomName).stream()
                    .map(BindingInfo::getRoutingKey)
                    .toList();
        }

        @Override
        public int countMessageOnQueue(final String queueName) {
            return this.client.getQueues(USERNAME).stream()
                    .filter(queue -> queue.getName().equals(queueName))
                    .findFirst()
                    .map(QueueInfo::getMessagesReady)
                    .orElse(0L).intValue();
        }


    }
}
