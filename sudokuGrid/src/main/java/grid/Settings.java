package grid;

import de.sfuhrm.sudoku.GameSchema;
import de.sfuhrm.sudoku.GameSchemas;

import java.io.Serial;
import java.io.Serializable;

public interface Settings extends Serializable {

    Schema schema();

    int size();

    Difficulty difficulty();

    default int maxNumbersToClear() {
        return this.difficulty().computeMaxNumbersToClear(this.schema());
    }


    enum Schema implements Serializable {
        SCHEMA_4x4(GameSchemas.SCHEMA_4X4, "4 X 4", GameSchemas.SCHEMA_4X4.getWidth()),
        SCHEMA_9x9(GameSchemas.SCHEMA_9X9, "9 X 9", GameSchemas.SCHEMA_9X9.getWidth()),
        SCHEMA_16X16(GameSchemas.SCHEMA_16X16, "16 X 16", GameSchemas.SCHEMA_16X16.getWidth()),
        SCHEMA_25X25(GameSchemas.SCHEMA_25X25, "25 X 25", GameSchemas.SCHEMA_25X25.getWidth());

        private final GameSchema schema;
        private final String name;
        private final int size;

        Schema(final GameSchema schema, final String name, final int size) {
            this.schema = schema;
            this.name = name;
            this.size = size;
        }

        public GameSchema schema() {
            return this.schema;
        }

        public String code() {
            return super.toString();
        }

        public int size() {
            return this.size;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    enum Difficulty implements Serializable {
        EASY(30),
        MEDIUM(40),
        HARD(50);

        private final int percent;

        Difficulty(final int percent) {
            this.percent = percent;
        }

        public int computeMaxNumbersToClear(final Schema schema) {
            return (schema.schema().getWidth() * schema.schema().getWidth() * this.percent) / 100;
        }

        public int percent() {
            return this.percent;
        }

        public String code() {
            return super.toString();
        }
    }


    record SettingsImpl(Schema schema, Difficulty difficulty) implements Settings {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public int size() {
            return this.schema.size;
        }

        @Override
        public String toString() {
            return "model.Settings: size[" + this.schema + "] difficulty[" + this.difficulty + "]";
        }

    }
}