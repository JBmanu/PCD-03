import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public final class Main {

    private final static String QUEUE_NAME = "hello";
    public static String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";

    public static void main(String[] args) {
        
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(URI);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String message = "Messaggio di prova da Java";

                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("Messaggio inviato: " + message);
            }

        } catch (Exception e) {
            System.err.println("Errore durante la connessione o l’invio:");
            e.printStackTrace();
        }
    }
}
