package model;

import grid.Grid;
import grid.Settings;

import java.util.List;
import java.util.Optional;

import static utils.Namespace.*;
import static utils.Namespace.PLAYER;
import static utils.Namespace.QUEUE;
import static utils.Namespace.SERVER;

public interface GridServer {
    static GridServer create() {
        return new GridServerImpl();
    }

    void createGrid(Settings settings);

    Optional<String> computeQueue(String countRoom);
    
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
        public Optional<String> computeQueue(final String countRoom) {
            return this.grid.map(_ -> 
                    String.join(DIVISOR, List.of(DOMAIN, ROOM, countRoom, QUEUE, SERVER)));
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
