package utils;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Delivery;
import grid.Coordinate;
import grid.FactoryGrid;
import grid.Grid;
import grid.Settings;
import utils.GameConsumers.CreationGrid;
import utils.GameConsumers.GridRequest;
import utils.GameConsumers.PlayerMove;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class Messages {
    public static final String TYPE_MESSAGE_KEY = "key";
    public static final String TYPE_GRID_REQUEST = "grid";
    public static final String TYPE_JOIN_PLAYER = "join";
    public static final String TYPE_LEAVE_PLAYER = "leave";
    public static final String TYPE_GRID = "gridData";
    public static final String TYPE_MOVE = "move";
    public static final String TYPE_FOCUS_GAINED = "focusGained";
    public static final String TYPE_FOCUS_LOST = "focusLost";

    public static final String ROW_KEY = "row";
    public static final String GRID_KEY = "grid";
    public static final String COL_KEY = "column";
    public static final String VALUE_KEY = "value";
    public static final String COLOR_KEY = "color";
    public static final String SCHEME_KEY = "scheme";
    public static final String DIFFICULTY_KEY = "difficulty";
    public static final String PLAYER_KEY = "player";
    public static final String COORDINATE_KEY = "coordinate";
    public static final String GRID_SOLUTION_KEY = "solution";

    public static final AMQP.BasicProperties JSON_PROPERTIES = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .build();

    private static final Gson GSON = new Gson();

    public static final class ToSend {

        public static String joinPlayer(final String playerName, final Color color) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_JOIN_PLAYER,
                    PLAYER_KEY, playerName,
                    COLOR_KEY, Map.of("r", color.getRed(), "g", color.getGreen(), "b", color.getBlue())));
        }

        public static String leavePlayer(final String playerName) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_LEAVE_PLAYER,
                    PLAYER_KEY, playerName));
        }

        public static String gridRequest(final String playerName) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_GRID_REQUEST,
                    PLAYER_KEY, playerName));
        }

        public static String move(final String playerName, final Coordinate coordinate, final int value) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_MOVE,
                    PLAYER_KEY, playerName,
                    COORDINATE_KEY, Map.of(ROW_KEY, coordinate.row(), COL_KEY, coordinate.col()),
                    VALUE_KEY, value));
        }

        public static String focusGained(final String playerName, final Coordinate coordinate, final Color color) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_FOCUS_GAINED,
                    PLAYER_KEY, playerName,
                    COORDINATE_KEY, Map.of(ROW_KEY, coordinate.row(), COL_KEY, coordinate.col()),
                    COLOR_KEY, Map.of("r", color.getRed(), "g", color.getGreen(), "b", color.getBlue())));
        }

        public static String focusLost(final String playerName, final Coordinate coordinate) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_FOCUS_LOST,
                    PLAYER_KEY, playerName,
                    COORDINATE_KEY, Map.of(ROW_KEY, coordinate.row(), COL_KEY, coordinate.col())));
        }

        public static String grid(final Grid grid, final String requesterName) {
            return GSON.toJson(Map.of(
                    TYPE_MESSAGE_KEY, TYPE_GRID,
                    SCHEME_KEY, grid.settings().schema().code(),
                    DIFFICULTY_KEY, grid.settings().difficulty().code(),
                    GRID_SOLUTION_KEY, grid.solutionArray(),
                    GRID_KEY, grid.cellsArray(),
                    PLAYER_KEY, requesterName));
        }
    }

    public static final class ToReceive {

        public static Map<String, Object> createMessage(final Delivery delivery) {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            return GSON.fromJson(message, Map.class);
        }

        public static void acceptGridRequest(final Delivery delivery, final GridRequest request) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            request.accept(playerName);
        }

        public static void acceptJoinPlayer(final Delivery delivery, final String myName,
                                            final GameConsumers.JoinPlayer joinPlayer) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            if (playerName.equals(myName)) return;
            final Map<String, Object> colorMap = (Map<String, Object>) data.get(COLOR_KEY);
            final int r = (int) ((double) colorMap.get("r"));
            final int g = (int) ((double) colorMap.get("g"));
            final int b = (int) ((double) colorMap.get("b"));
            joinPlayer.accept(playerName, new Color(r, g, b));
        }

        public static void acceptLeavePlayer(final Delivery delivery, final String myName,
                                             final GameConsumers.LeavePlayer leavePlayer) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            if (playerName.equals(myName)) return;
            leavePlayer.accept(playerName);
        }

        public static void acceptMove(final Delivery delivery, final String myName,
                                      final PlayerMove action) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            if (playerName.equals(myName)) return;
            final Map<String, Object> coordinate = (Map<String, Object>) data.get(COORDINATE_KEY);
            final int row = (int) ((double) coordinate.get(ROW_KEY));
            final int column = (int) ((double) coordinate.get(COL_KEY));
            final int value = (int) ((double) data.get(VALUE_KEY));
            action.accept(playerName, FactoryGrid.coordinate(row, column), value);
        }

        public static void acceptGrid(final Delivery delivery, final String myName,
                                      final CreationGrid initGrid) {
            final Map<String, Object> data = createMessage(delivery);
            final String requesterName = (String) data.get(PLAYER_KEY);
            if (!requesterName.equals(myName)) return;
            final Settings.Schema schema = Settings.Schema.valueOf(((String) data.get(SCHEME_KEY)));
            final Settings.Difficulty difficulty = Settings.Difficulty.valueOf((String) data.get(DIFFICULTY_KEY));
            final byte[][] gridArray = GSON.fromJson(data.get(GRID_KEY).toString(), byte[][].class);
            final byte[][] solutionArray = GSON.fromJson(data.get(GRID_SOLUTION_KEY).toString(), byte[][].class);
            initGrid.accept(schema, difficulty, solutionArray, gridArray);
        }

        public static void acceptFocusGained(final Delivery delivery, final String myName,
                                             final GameConsumers.FocusGained action) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            if (playerName.equals(myName)) return;
            final Map<String, Object> coordinate = (Map<String, Object>) data.get(COORDINATE_KEY);
            final Map<String, Object> colorMap = (Map<String, Object>) data.get(COLOR_KEY);
            final int row = (int) ((double) coordinate.get(ROW_KEY));
            final int col = (int) ((double) coordinate.get(COL_KEY));
            final int r = (int) ((double) colorMap.get("r"));
            final int g = (int) ((double) colorMap.get("g"));
            final int b = (int) ((double) colorMap.get("b"));
            action.accept(playerName, new Color(r, g, b), FactoryGrid.coordinate(row, col));
        }

        public static void acceptFocusLost(final Delivery delivery, final String myName,
                                           final GameConsumers.FocusLost action) {
            final Map<String, Object> data = createMessage(delivery);
            final String playerName = (String) data.get(PLAYER_KEY);
            if (playerName.equals(myName)) return;
            final Map<String, Object> coordinate = (Map<String, Object>) data.get(COORDINATE_KEY);
            final int row = (int) ((double) coordinate.get(ROW_KEY));
            final int col = (int) ((double) coordinate.get(COL_KEY));
            action.accept(playerName, FactoryGrid.coordinate(row, col));
        }
    }
}
