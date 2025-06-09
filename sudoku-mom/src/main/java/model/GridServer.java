package model;

import grid.Grid;
import grid.Settings;

import java.util.Optional;

public interface GridServer {
    static GridServer create() {
        return new GridServerImpl();
    }

    void createGrid(Settings settings);

    Optional<byte[][]> solution();

    Optional<byte[][]> grid();


    class GridServerImpl implements GridServer {
        private Optional<Grid> grid;

        public GridServerImpl() {
            this.grid = Optional.empty();
        }

        @Override
        public void createGrid(final Settings settings) {
            this.grid = Optional.of(Grid.create(settings));
        }

        @Override
        public Optional<byte[][]> solution() {
            return this.grid.map(Grid::solutionArray);
        }

        @Override
        public Optional<byte[][]> grid() {
            return this.grid.map(Grid::cellsArray);
        }
    }
}
