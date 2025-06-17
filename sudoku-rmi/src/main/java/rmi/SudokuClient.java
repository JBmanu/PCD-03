package rmi;

import grid.Grid;

import java.util.Optional;

public interface SudokuClient {

    static Optional<SudokuClient> create(final String name, final int roomId) {
        return Optional.of(new SudokuClientImpl(name, roomId));
    }


    int roomId();

    void retrieveGrid(Grid grid);

    SudokuClient setId(int id);

    default SudokuClient copyId(final SudokuClient client) {
        return this.setId(client.roomId());
    }


    record SudokuClientImpl(String name, int roomId) implements SudokuClient {

        @Override
        public void retrieveGrid(final Grid grid) {

        }

        @Override
        public SudokuClient setId(final int id) {
            return new SudokuClientImpl(this.name, id);
        }
    }
}
