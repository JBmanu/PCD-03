import player.Player;

public final class Main {

    private final static String QUEUE_NAME = "hello";
    public static String URI = "amqp://fanltles:6qCOcwZEWGpkuiJnzfvybUUeXfHy1oM0@kangaroo.rmq.cloudamqp.com/fanltles";

    public static void main(final String[] args) {
        final RoomPlayerQueueInspector inspector = RoomPlayerQueueInspector.create();
        final Player player = Player.create();
        
        final int roomCount = inspector.countRooms();
        final int queueCount = inspector.countQueues();

        
        
//        System.out.println("Room count: " + roomCount + " - Queue: " + queueCount);
        
//        player.createRoom(1+"", 1+"", "Manu");
        
    }
    
}
