import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;
import com.rabbitmq.http.client.domain.ChannelInfo;
import com.rabbitmq.http.client.domain.ConnectionInfo;
import com.rabbitmq.http.client.domain.ExchangeInfo;
import com.rabbitmq.http.client.domain.QueueInfo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public interface GameRoomQueueDiscovery {
    String DEFAULT_EXCHANGE_NAME = "";
    int COUNT_DEFAULT_EXCHANGE = 7;

    static GameRoomQueueDiscovery create() {
        return new GameRoomQueueDiscoveryImpl();
    }

    int countExchanges();

    int countExchangesWithName(String name);

    int countExchangesContains(String subString);

    int countQueues();

    int countQueuesWithName(String name);

    int countQueuesContains(String subString);


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

        private void exchanges() {
            final List<ExchangeInfo> exchanges = this.client.getExchanges();
            for (final ExchangeInfo exchange : exchanges) {
                System.out.println("Exchange: " + exchange.getName() + ", Tipo: " + exchange.getType());
            }
        }

        private void queues() {
            final List<QueueInfo> queues = this.client.getQueues();
            for (final QueueInfo queue : queues) {
                System.out.println("Coda: " + queue.getName() + ", Messaggi: " + queue.getMessageStats().toString());
            }
        }



        private void connections() {
            final List<ConnectionInfo> connections = this.client.getConnections();
            for (final ConnectionInfo connection : connections) {
                System.out.println("Connessione: " + connection.getName());
            }
        }

        private void channels() {
            final List<ChannelInfo> channels = this.client.getChannels();
            for (final ChannelInfo channel : channels) {
                System.out.println("Canale: " + channel.getName());
            }
        }

        @Override
        public int countExchanges() {
            return this.client.getExchanges().size();
        }

        @Override
        public int countExchangesWithName(final String name) {
            return (int) this.client.getExchanges().stream()
                    .filter(exchangeInfo -> exchangeInfo.getName().equals(name))
                    .count();
        }

        @Override
        public int countExchangesContains(final String subString) {
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
        public int countQueuesContains(final String subString) {
            return (int) this.client.getQueues().stream()
                    .filter(queue -> queue.getName().contains(subString))
                    .count();
        }
    }
}
