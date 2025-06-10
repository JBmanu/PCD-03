package utils;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Delivery;
import grid.Coordinate;
import grid.Grid;
import utils.GameConsumers.GridData;
import utils.GameConsumers.GridRequest;
import utils.GameConsumers.PlayerAction;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class Messages {
    public static final String REQUEST = "request";
    public static final String REQUEST_GRID = "grid";
    
    private static final Gson GSON = new Gson();
    
    public static final AMQP.BasicProperties JSON_PROPERTIES = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .build();

    public static final class ToSend {

        public static String move(final String playerName, final Coordinate coordinate, final int value) {
            return GSON.toJson(
                    Map.of(
                            "player", playerName,
                            "coordinate", Map.of("row", coordinate.row(), "column", coordinate.col()),
                            "value", value
                    )
            );
        }

        public static String requestGrid(final String playerName) {
            return GSON.toJson(Map.of("player", playerName, REQUEST, REQUEST_GRID));
        }

        public static String grid(final Grid grid) {
            final byte[][] solution = grid.solutionArray();
            final byte[][] gridArray = grid.cellsArray();
            return GSON.toJson(Map.of("solution", solution, "grid", gridArray));

        }
    }

    public static final class ToReceive {

        public static void acceptMove(final Delivery delivery, final PlayerAction action) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final String playerName = (String) data.get("player");
            final Map<String, Object> coordinate = (Map<String, Object>) data.get("coordinate");
            final int row = (int) ((double) coordinate.get("row"));
            final int column = (int) ((double) coordinate.get("column"));
            final int value = (int) ((double) data.get("value"));
            action.accept(playerName, Coordinate.create(row, column), value);
        }

        public static void acceptGridRequest(final Delivery delivery, final GridRequest request) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final String playerName = (String) data.get("player");
            final String infoRequest = (String) data.get(REQUEST);
            if (!REQUEST_GRID.equals(infoRequest)) {
                throw new RuntimeException("Received unexpected request: " + infoRequest);
            }
            request.accept(playerName);
        }

        public static void acceptGrid(final Delivery delivery, final GridData gridData) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final byte[][] gridArray = GSON.fromJson(data.get("grid").toString(), byte[][].class);
            final byte[][] solutionArray = GSON.fromJson(data.get("solution").toString(), byte[][].class);
            gridData.accept(solutionArray, gridArray);
        }
    }


}
