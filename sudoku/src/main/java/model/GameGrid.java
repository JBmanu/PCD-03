package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

import java.util.Map;

public interface GameGrid {

    Map<Coordinate, Integer> solution();

    Map<Coordinate, Integer> toBeSolved();


    class GameGridImpl implements GameGrid {
        private final GameMatrix solution;
        private final GameMatrix gameMatrix;

        public GameGridImpl(final Settings settings) {
            final Settings.Size size = settings.size();
            final int maxNumbersToClear = settings.difficulty().computeMaxNumbersToClear(size);

            this.solution = Creator.createFull(size.schema());
            this.gameMatrix = Creator.createRiddle(this.solution, maxNumbersToClear);
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
        public Map<Coordinate, Integer> solution() {
            return this.convertToMap(this.solution);
        }

        @Override
        public Map<Coordinate, Integer> toBeSolved() {
            return this.convertToMap(this.gameMatrix);
        }
    }


}
