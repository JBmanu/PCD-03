package model.settings;

import de.sfuhrm.sudoku.GameSchema;
import de.sfuhrm.sudoku.GameSchemas;

public enum GridSize {
    SCHEMA_4x4(GameSchemas.SCHEMA_4X4, "4 X 4"),
    SCHEMA_9x9(GameSchemas.SCHEMA_9X9, "9 X 9"),
    SCHEMA_16X16(GameSchemas.SCHEMA_16X16, "16 X 16"),
    SCHEMA_25X25(GameSchemas.SCHEMA_25X25, "25 X 25");

    private final GameSchema schema;
    private final String name;

    GridSize(final GameSchema schema, final String name) {
        this.schema = schema;
        this.name = name;
    }

    public GameSchema schema() {
        return this.schema;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
