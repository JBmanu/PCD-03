package player;

//other name: GameRoomQueueDiscovery

import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;
import com.rabbitmq.http.client.domain.ChannelInfo;
import com.rabbitmq.http.client.domain.ConnectionInfo;
import com.rabbitmq.http.client.domain.ExchangeInfo;
import com.rabbitmq.http.client.domain.QueueInfo;

import java.util.List;

public class RoomPlayerQueueInspector {

    public static void main(final String[] args) throws Exception {
        final String host = "kangaroo.rmq.cloudamqp.com";
        final String username = "fanltles";
        final String password = "6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0";
        
        final Client client = new Client(new ClientParameters()
                .url("https://" + host + "/api/")
                .username(username)
                .password(password));

        // Ottenere le code
        final List<QueueInfo> queues = client.getQueues();
        for (final QueueInfo queue : queues) {
            System.out.println("Coda: " + queue.getName() + ", Messaggi: " + queue.getMessageStats().toString());
        }

        // Ottenere gli exchange
        final List<ExchangeInfo> exchanges = client.getExchanges();
        for (final ExchangeInfo exchange : exchanges) {
            System.out.println("Exchange: " + exchange.getName() + ", Tipo: " + exchange.getType());
        }

        // Ottenere le connessioni
        final List<ConnectionInfo> connections = client.getConnections();
        for (final ConnectionInfo connection : connections) {
            System.out.println("Connessione: " + connection.getName());
        }

        // Ottenere i canali
        final List<ChannelInfo> channels = client.getChannels();
        for (final ChannelInfo channel : channels) {
            System.out.println("Canale: " + channel.getName());
        }
    }
}
