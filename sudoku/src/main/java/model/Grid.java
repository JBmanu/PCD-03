package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

import java.util.HashMap;
import java.util.Map;

public interface Grid {
    static Grid create(final Settings settings) {
        return new GridImpl(settings);
    }

    int emptyValue = 0;
    
    int size();

    boolean isValidSolution();
    
    Map<Coordinate, Integer> solution();
    Map<Coordinate, Integer> startGrid();
    

    class GridImpl implements Grid {
        private final Settings settings;
        private final GameMatrix solution;
        private final GameMatrix startGrid;
        private final GameMatrix currentGrid;

        public GridImpl(final Settings settings) {
            this.settings = settings;
            this.solution = Creator.createFull(settings.schema().schema());
            this.startGrid = Creator.createRiddle(this.solution, settings.maxNumbersToClear());
            this.currentGrid = this.startGrid;
        }
        
        @Override
        public int size() {
            return this.settings.size();
        }

        @Override
        public boolean isValidSolution() {
            return this.solution.isValid();
        }

        private Map<Coordinate, Integer> convertToMap(final GameMatrix matrix) {
            final Map<Coordinate, Integer> cells = new HashMap<>();
            final int length = matrix.getSchema().getWidth();
            for (int row = 0; row < length; row++) {
                for (int column = 0; column < length; column++) {
                    final Coordinate position = Coordinate.create(row, column);
                    final int value = matrix.get(row, column);
                    cells.put(position, value);
                }
            }
            return cells;
        }

        @Override
        public Map<Coordinate, Integer> solution() {
            return this.convertToMap(this.solution);
        }

        @Override
        public Map<Coordinate, Integer> startGrid() {
            return this.convertToMap(this.startGrid);
        }


    }
}
