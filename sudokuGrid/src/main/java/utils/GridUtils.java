package utils;

import grid.Coordinate;
import grid.FactoryGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class GridUtils {

    public static List<Coordinate> computeQuadrant(final Coordinate coordinate, final int size) {
        final int row = coordinate.row();
        final int col = coordinate.col();
        final List<Coordinate> coordinates = new ArrayList<>();

        final int sizeQuadrant = (int) Math.sqrt(size);
        final int rowStart = (row / sizeQuadrant) * sizeQuadrant;
        final int colStart = (col / sizeQuadrant) * sizeQuadrant;

        for (int i = 0; i < sizeQuadrant; i++)
            for (int j = 0; j < sizeQuadrant; j++)
                coordinates.add(FactoryGrid.coordinate(rowStart + i, colStart + j));

        return coordinates;
    }

    public static List<Coordinate> createRowAndColFrom(final Coordinate coordinate, final int size) {
        final int row = coordinate.row();
        final int col = coordinate.col();

        return new java.util.ArrayList<>(IntStream.range(0, size)
                .mapToObj(i -> List.of(FactoryGrid.coordinate(row, i), FactoryGrid.coordinate(i, col)))
                .flatMap(List::stream)
                .toList());
    }

    public static boolean compareArrays(final byte[][] a, final byte[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) return false;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }
        return true;
    }
}