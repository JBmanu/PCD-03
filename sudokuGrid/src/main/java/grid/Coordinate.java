package grid;

import java.io.Serializable;

public interface Coordinate extends Serializable {

    int row();

    int col();
    

    record CoordinateImpl(int row, int col) implements Coordinate {
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "POS: row[" + this.row + "] column[" + this.col + "]";
        }
    }
}
