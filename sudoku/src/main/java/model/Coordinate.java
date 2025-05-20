package model;

public interface Coordinate {
    int row();

    int column();
    
    static Coordinate create(final int row, final int column) {
        return new CoordinateImpl(row, column);
    }

    record CoordinateImpl(int row, int column) implements Coordinate {
        
        @Override
        public String toString() {
            return "POS: row[" + this.row + "] column[" + this.column + "]";
        }
    }
}
