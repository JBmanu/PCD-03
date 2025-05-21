package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

import java.util.*;

public interface Grid {
    static Grid create(final Settings settings) {
        return new GridImpl(settings);
    }


    int emptyValue();

    int countEmptyValue();

    int size();

    boolean isValidSolution();

    boolean hasWin();

    boolean isGridCreateFromSolution();

    Map<Coordinate, Integer> solution();

    Map<Coordinate, Integer> grid();

    List<Map.Entry<Coordinate, Integer>> emptyCells();


    int valueFrom(Coordinate coordinate);

    void setValue(Coordinate coordinate, int value);

    void suggest();

    void back();


    class GridImpl implements Grid {
        private final Settings settings;
        private final GameMatrix solution;
        private final GameMatrix grid;
        private final Stack<Map.Entry<Coordinate, Integer>> historyAction;

        public GridImpl(final Settings settings) {
            this.settings = settings;
            this.historyAction = new Stack<>();
            this.solution = Creator.createFull(settings.schema().schema());
            this.grid = Creator.createRiddle(this.solution, settings.maxNumbersToClear());
        }

        @Override
        public int emptyValue() {
            return 0;
        }

        @Override
        public List<Map.Entry<Coordinate, Integer>> emptyCells() {
            return this.grid().entrySet().stream()
                    .filter(entry -> entry.getValue().equals(this.emptyValue()))
                    .toList();
        }

        @Override
        public int countEmptyValue() {
            return this.emptyCells().size();
        }

        @Override
        public int size() {
            return this.settings.size();
        }

        @Override
        public boolean isValidSolution() {
            return this.solution.isValid();
        }

        @Override
        public boolean hasWin() {
            return this.grid.isValid();
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
        public Map<Coordinate, Integer> grid() {
            return this.convertToMap(this.grid);
        }

        @Override
        public boolean isGridCreateFromSolution() {
            final Map<Coordinate, Integer> solution = this.solution();
            final int zeroDifferent = 0;

            final long countDifferentValue = this.grid().entrySet().stream()
                    .filter(entry ->
                            !entry.getValue().equals(this.emptyValue()) &&
                                    !entry.getValue().equals(solution.get(entry.getKey())))
                    .count();

            return countDifferentValue == zeroDifferent;
        }

        @Override
        public int valueFrom(final Coordinate coordinate) {
            return this.grid.get(coordinate.row(), coordinate.column());
        }

        @Override
        public void setValue(final Coordinate coordinate, final int value) {
            this.grid.set(coordinate.row(), coordinate.column(), (byte) value);
            this.historyAction.push(Map.entry(coordinate, value));
        }

        @Override
        public void suggest() {
            final Optional<Map.Entry<Coordinate, Integer>> firstCleanCell = this.emptyCells().stream().findFirst();

            firstCleanCell.ifPresent(entry -> {
                final Coordinate position = entry.getKey();
                final int value = this.solution.get(position.row(), position.column());
                this.setValue(position, value);
            });
        }

        @Override
        public void back() {
            if (this.historyAction.empty()) return;
            final Map.Entry<Coordinate, Integer> firstAction = this.historyAction.pop();
            this.setValue(firstAction.getKey(), this.emptyValue());
        }

    }
}
