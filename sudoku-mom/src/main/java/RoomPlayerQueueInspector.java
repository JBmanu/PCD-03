import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;
import com.rabbitmq.http.client.domain.ChannelInfo;
import com.rabbitmq.http.client.domain.ConnectionInfo;
import com.rabbitmq.http.client.domain.ExchangeInfo;
import com.rabbitmq.http.client.domain.QueueInfo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

//other name: GameRoomQueueDiscovery
public interface RoomPlayerQueueInspector {
    
    static RoomPlayerQueueInspector create() {
        return new RoomPlayerQueueInspectorImpl();
    }
    
    int countRooms();
    
    int countQueues();
    
    class RoomPlayerQueueInspectorImpl implements RoomPlayerQueueInspector {
        private static final String HOST = "kangaroo.rmq.cloudamqp.com";
        private static final String USERNAME = "fanltles";
        private static final String PASSWORD = "6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0";
        private static final String URL = "https://" + HOST + "/api/";

        private final Client client;

        public RoomPlayerQueueInspectorImpl() {
            try {
                this.client = new Client(new ClientParameters()
                        .url(URL)
                        .username(USERNAME)
                        .password(PASSWORD));
            } catch (final URISyntaxException | MalformedURLException e) {
                throw new RuntimeException("Failed to create RabbitMQ HTTP client", e);
            }
        }

        private void queues() {
            final List<QueueInfo> queues = this.client.getQueues();
            for (final QueueInfo queue : queues) {
                System.out.println("Coda: " + queue.getName() + ", Messaggi: " + queue.getMessageStats().toString());
            }
        }

        private void exchanges() {
            final List<ExchangeInfo> exchanges = this.client.getExchanges();
            for (final ExchangeInfo exchange : exchanges) {
                System.out.println("Exchange: " + exchange.getName() + ", Tipo: " + exchange.getType());
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
        public int countRooms() {
            return this.client.getExchanges().size() - 6;
        }

        @Override
        public int countQueues() {
            return this.client.getQueues().size() + 1;
        }
    }
}
