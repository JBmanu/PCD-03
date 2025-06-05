import org.junit.jupiter.api.Test;
import rabbitMQ.RabbitMQConnector;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RabbitMQConnectorTest {

    private RabbitMQConnector connector;

    @Test
    public void create() {
        await().until(() -> {
            this.connector = RabbitMQConnector.create();
            return true;
        });
        assertNotNull(this.connector);
    }
    
}
