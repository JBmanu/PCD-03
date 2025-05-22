package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Grid {
    static Grid create(final Settings settings) {
        return new GridImpl(settings);
    }


    int size();
    
    int emptyValue();

    int countEmptyValue();

    boolean hasWin();
    
    boolean isValidSolution();

    boolean isGridCreateFromSolution();


    Map<Coordinate, Integer> solution();

    Map<Coordinate, Integer> grid();

    List<Map.Entry<Coordinate, Integer>> emptyCells();


    void saveValue(Coordinate coordinate, int value);

    int valueFrom(Coordinate coordinate);

    void suggest();

    void undo();

    void reset();


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

        private void setValue(final Coordinate coordinate, final int value) {
            this.grid.set(coordinate.row(), coordinate.column(), (byte) value);
        }

        @Override
        public int size() {
            return this.settings.size();
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
        public boolean hasWin() {
            return this.grid.isValid();
        }

        @Override
        public boolean isValidSolution() {
            return this.solution.isValid();
        }

        @Override
        public boolean isGridCreateFromSolution() {
            final Map<Coordinate, Integer> solution = this.solution();
            final int zeroDifferent = 0;

            final long countDifferentValue = this.grid().entrySet().stream()
                    .filter(entry -> !entry.getValue().equals(this.emptyValue())) 
                    .filter(entry -> !entry.getValue().equals(solution.get(entry.getKey())))
                    .count();

            return countDifferentValue == zeroDifferent;
        }

        private Map<Coordinate, Integer> convertToMap(final GameMatrix matrix) {
            final List<Integer> rangeSize = IntStream.range(0, this.size()).boxed().toList();
            final Stream<Coordinate> coordinates = rangeSize.stream().flatMap(row -> rangeSize.stream()
                    .map(column -> Coordinate.create(row, column)));

            return coordinates.map(coord -> Map.entry(coord, matrix.get(coord.row(), coord.column())))
                    .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
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
        public void saveValue(final Coordinate coordinate, final int value) {
            this.setValue(coordinate, value);
            this.historyAction.push(Map.entry(coordinate, value));
        }

        @Override
        public int valueFrom(final Coordinate coordinate) {
            return this.grid.get(coordinate.row(), coordinate.column());
        }

        @Override
        public void suggest() {
            final Optional<Map.Entry<Coordinate, Integer>> firstCleanCell = this.emptyCells().stream().findFirst();

            firstCleanCell.ifPresent(entry -> {
                final Coordinate position = entry.getKey();
                final int value = this.solution.get(position.row(), position.column());
                this.saveValue(position, value);
            });
        }

        @Override
        public void undo() {
            if (this.historyAction.empty()) return;
            final Map.Entry<Coordinate, Integer> firstAction = this.historyAction.pop();
            this.saveValue(firstAction.getKey(), this.emptyValue());
        }

        @Override
        public void reset() {
            this.historyAction.stream().map(Map.Entry::getKey).forEach(entry -> this.setValue(entry, this.emptyValue()));
            this.historyAction.clear();
        }

    }
}
