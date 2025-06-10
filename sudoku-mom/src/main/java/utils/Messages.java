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
    // KEYS
    public static final String ROW_KEY = "row";
    public static final String GRID_KEY = "grid";
    public static final String COL_KEY = "column";
    public static final String VALUE_KEY = "value";
    
    public static final String PLAYER_KEY = "player";
    public static final String GRID_REQUEST_KEY = "gridRequest";
    public static final String COORDINATE_KEY = "coordinate";
    public static final String GRID_SOLUTION_KEY = "solution";
    
    // VALUES
    public static final String GRID = "grid";
    
    public static final AMQP.BasicProperties JSON_PROPERTIES = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .build();

    private static final Gson GSON = new Gson();

    public static final class ToSend {

        public static String move(final String playerName, final Coordinate coordinate, final int value) {
            return GSON.toJson(
                    Map.of(
                            PLAYER_KEY, playerName,
                            COORDINATE_KEY, Map.of(ROW_KEY, coordinate.row(), COL_KEY, coordinate.col()),
                            VALUE_KEY, value
                    )
            );
        }

        public static String requestGrid(final String playerName) {
            return GSON.toJson(Map.of(PLAYER_KEY, playerName, GRID_REQUEST_KEY, GRID));
        }

        public static String grid(final Grid grid) {
            final byte[][] solution = grid.solutionArray();
            final byte[][] gridArray = grid.cellsArray();
            return GSON.toJson(Map.of(GRID_SOLUTION_KEY, solution, GRID_KEY, gridArray));
        }
    }

    public static final class ToReceive {

        public static void acceptMove(final Delivery delivery, final PlayerAction action) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final String playerName = (String) data.get(PLAYER_KEY);
            final Map<String, Object> coordinate = (Map<String, Object>) data.get(COORDINATE_KEY);
            final int row = (int) ((double) coordinate.get(ROW_KEY));
            final int column = (int) ((double) coordinate.get(COL_KEY));
            final int value = (int) ((double) data.get(VALUE_KEY));
            action.accept(playerName, Coordinate.create(row, column), value);
        }

        public static void acceptGridRequest(final Delivery delivery, final GridRequest request) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final String playerName = (String) data.get(PLAYER_KEY);
            final String infoRequest = (String) data.get(GRID_REQUEST_KEY);
            if (!GRID.equals(infoRequest)) {
                throw new RuntimeException("Received unexpected request: " + infoRequest);
            }
            request.accept(playerName);
        }

        public static void acceptGrid(final Delivery delivery, final GridData gridData) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            final Map<String, Object> data = GSON.fromJson(message, Map.class);
            final byte[][] gridArray = GSON.fromJson(data.get(GRID_KEY).toString(), byte[][].class);
            final byte[][] solutionArray = GSON.fromJson(data.get(GRID_SOLUTION_KEY).toString(), byte[][].class);
            gridData.accept(solutionArray, gridArray);
        }
    }


}
