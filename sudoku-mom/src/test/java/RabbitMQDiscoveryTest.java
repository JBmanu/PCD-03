import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rabbitMQ.RabbitMQDiscovery;

import java.util.List;
import java.util.Optional;

import static rabbitMQ.RabbitMQDiscovery.COUNT_DEFAULT_EXCHANGE;
import static rabbitMQ.RabbitMQDiscovery.DEFAULT_EXCHANGE_NAME;
import static org.junit.jupiter.api.Assertions.*;

public class RabbitMQDiscoveryTest {

    private RabbitMQDiscovery discovery;

    @BeforeEach
    public void create() {
        final Optional<RabbitMQDiscovery> discoveryOpt = RabbitMQDiscovery.create();
        assertTrue(discoveryOpt.isPresent());
        this.discovery = discoveryOpt.get();
        assertNotNull(this.discovery);
    }
    
    @Test
    public void countDefaultExchanges() {
        assertEquals(COUNT_DEFAULT_EXCHANGE, this.discovery.countExchanges());
    }
    
    @Test
    public void countExchangesWithoutDefault() {
        assertEquals(0, this.discovery.countExchangesWithoutDefault());        
    }
    
    @Test
    public void countExchangesWithName() {
        final int count = this.discovery.countExchangesWithName(DEFAULT_EXCHANGE_NAME);
        assertEquals(1, count);
    }
    
    @Test
    public void countExchangesThatContainsName() {
        final int count = this.discovery.countExchangesThatContains("amq");
        assertEquals(COUNT_DEFAULT_EXCHANGE - 1, count);
    }

    @Test
    public void countQueues() {
        final int count = this.discovery.countQueues();
        assertEquals(0, count);
    }
    
    @Test
    public void countQueuesWithName() {
        final int count = this.discovery.countQueuesWithName("");
        assertEquals(0, count);
    }
    
    @Test
    public void countQueuesThatContainsName() {
        final int count = this.discovery.countQueuesThatContains("queue");
        assertEquals(0, count);
    }
    
    @Test
    public void countExchangeBinds() {
        final int count = this.discovery.countExchangeBinds("amq");
        assertEquals(0, count);
    }
    
    @Test
    public void countQueueBinds() {
        final int count = this.discovery.countQueueBinds("queue");
        assertEquals(0, count);
    }
    
    @Test
    public void routingKeysFromBindsExchange() {
        final List<String> queues = this.discovery.routingKeysFromBindsExchange("amq");
        assertEquals(0, queues.size());
    }
    
    @Test
    public void routingKeysFromBindsQueueWithoutQueue() {
        final List<String> queues = this.discovery.routingKeysFromBindsExchange("amq", "");
        assertEquals(0, queues.size());
    }
    
    @Test
    public void countMessageOnQueue() {
        assertEquals(0, this.discovery.countMessageOnQueue("amq"));
    }

}
