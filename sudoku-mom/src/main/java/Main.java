import player.Player;
import rabbitMQ.GameRoomQueueDiscovery;
import rabbitMQ.RabbitMQConnector;

public final class Main {

    public static void main(final String[] args) {
        final GameRoomQueueDiscovery discovery = GameRoomQueueDiscovery.create();
        final RabbitMQConnector connector = RabbitMQConnector.create();
        final Player player = Player.create();
        
        final int roomCount = discovery.countExchanges();
        final int queueCount = discovery.countQueues();

//        connector.createRoom("room1", "queue1", "manu");
        
//        await().until(() -> {
//            this.player = Player.create();
//            return true;
//        });
        
//        System.out.println("Room count: " + roomCount + " - Queue: " + queueCount);
        
//        player.createRoom(1+"", 1+"", "Manu");
        
    }
    
}
