package grid;

public interface Coordinate {

    static Coordinate create(final int row, final int column) {
        return new CoordinateImpl(row, column);
    }
    

    int row();

    int column();
    

    record CoordinateImpl(int row, int column) implements Coordinate {

        @Override
        public String toString() {
            return "POS: row[" + this.row + "] column[" + this.column + "]";
        }
    }
}
