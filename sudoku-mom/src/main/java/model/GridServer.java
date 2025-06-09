package model;

import grid.Coordinate;
import grid.Grid;
import grid.Settings;
import utils.Namespace;

import java.util.Optional;

public interface GridServer {
    static GridServer create() {
        return new GridServerImpl();
    }

    void createGrid(Settings settings);

    void createGameData(String countRoom, String countQueue);

    Optional<byte[][]> solution();

    Optional<byte[][]> grid();

    Optional<String> room();

    Optional<String> queue();

    void updateGrid(Coordinate coordinate, int value);

    Optional<Integer> value(Coordinate coordinate);


    class GridServerImpl implements GridServer {
        private Optional<Grid> grid;
        private Optional<String> room;
        private Optional<String> queue;


        public GridServerImpl() {
            this.grid = Optional.empty();
            this.room = Optional.empty();
            this.queue = Optional.empty();
        }

        @Override
        public void createGrid(final Settings settings) {
            this.grid = Optional.of(Grid.create(settings));
        }

        @Override
        public void createGameData(final String countRoom, final String countQueue) {
            this.room = this.grid.map(_ -> Namespace.computeRoomName(countRoom));
            this.queue = this.grid.map(_ -> Namespace.computeServerQueueName(countQueue));
        }

        @Override
        public Optional<byte[][]> solution() {
            return this.grid.map(Grid::solutionArray);
        }

        @Override
        public Optional<byte[][]> grid() {
            return this.grid.map(Grid::cellsArray);
        }

        @Override
        public Optional<String> room() {
            return this.room;
        }

        @Override
        public Optional<String> queue() {
            return this.queue;
        }

        @Override
        public void updateGrid(final Coordinate coordinate, final int value) {
            this.grid.ifPresent(grid -> grid.saveValue(coordinate, value));
        }

        @Override
        public Optional<Integer> value(final Coordinate coordinate) {
            return this.grid.map(grid -> grid.valueFrom(coordinate));
        }
    }
}
