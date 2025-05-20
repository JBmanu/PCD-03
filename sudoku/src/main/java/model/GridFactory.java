package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

import java.util.Map;

public interface GridFactory {

    Grid create();

    static GridFactory createFactory(final Settings settings) {
        return new GridFactoryImpl(settings);
    }

    record GridFactoryImpl(Settings settings) implements GridFactory {

        private GameMatrix createSolution() {
            return Creator.createFull(this.settings.size().schema());
        }

        private GameMatrix createRiddle(final GameMatrix solution) {
            final int maxNumbersToClear = this.settings.difficulty().computeMaxNumbersToClear(this.settings.size());
            return Creator.createRiddle(solution, maxNumbersToClear);
        }

        private Map<Coordinate, Integer> convertToMap(final GameMatrix matrix) {
            final Map<Coordinate, Integer> cells = new java.util.HashMap<>();
            final int length = matrix.getSchema().getWidth();
            for (int row = 0; row < length; row++) {
                for (int column = 0; column < length; column++) {
                    final Coordinate coordinate = Coordinate.create(row, column);
                    final int value = matrix.get(row, column);
                    cells.put(coordinate, value);
                }
            }
            return cells;
        }

        @Override
        public Grid create() {
            final Map<Coordinate, Integer> cells = this.convertToMap(this.createSolution());
            final Map<Coordinate, Integer> riddle = this.convertToMap(this.createRiddle(this.createSolution()));
            
            return null;
        }
    }
}
