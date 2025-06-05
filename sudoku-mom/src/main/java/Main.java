import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public final class Main {

    private final static String QUEUE_NAME = "hello";
    public static String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";

    public static void main(final String[] args) {
        
        final ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(URI);

            try (final Connection connection = factory.newConnection();
                 final Channel channel = connection.createChannel()) {
                
                // Nome della coda e dell'exchange da eliminare
                final String queueName = QUEUE_NAME;
                final String exchangeName = "(AMQP default)";

                // Elimina la coda
                channel.queueDelete(queueName);
                System.out.println("Coda eliminata: " + queueName);

                // Elimina l'exchange
                channel.exchangeDelete(exchangeName);
                System.out.println("Exchange eliminato: " + exchangeName);
            }

        } catch (final Exception e) {
            System.err.println("Errore durante la connessione o l’invio:");
            e.printStackTrace();
        }
    }
}
