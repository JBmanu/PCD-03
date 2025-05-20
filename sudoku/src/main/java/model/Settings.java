package model;

import de.sfuhrm.sudoku.GameSchema;
import de.sfuhrm.sudoku.GameSchemas;

public interface Settings {

    static Settings createSettings(final Size size, final Difficulty difficulty) {
        return new SettingsImpl(size, difficulty);
    }


    Size size();

    Difficulty difficulty();


    enum Size {
        SCHEMA_4x4(GameSchemas.SCHEMA_4X4, "4 X 4"),
        SCHEMA_9x9(GameSchemas.SCHEMA_9X9, "9 X 9"),
        SCHEMA_16X16(GameSchemas.SCHEMA_16X16, "16 X 16"),
        SCHEMA_25X25(GameSchemas.SCHEMA_25X25, "25 X 25");

        private final GameSchema schema;
        private final String name;

        Size(final GameSchema schema, final String name) {
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

    enum Difficulty {
        EASY(30),
        MEDIUM(40),
        HARD(50);
        
        private final int percent;

        Difficulty(final int percent) {
            this.percent = percent;
        }

        public int computeMaxNumbersToClear(final Size size) {
            return (size.schema().getWidth() * size.schema().getWidth() * this.percent) / 100;
        }

        public int percent() {
            return this.percent;
        }
    }


    record SettingsImpl(Size size, Difficulty difficulty) implements Settings {

        @Override
        public String toString() {
            return "Settings: size[" + this.size + "] difficulty[" + this.difficulty + "]";
        }
    }
}