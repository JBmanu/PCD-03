package utils;

import model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class GridUtils {

    public static List<Coordinate> computeQuadrant(final Coordinate coordinate, final int size) {
        final int row = coordinate.row();
        final int col = coordinate.column();
        final List<Coordinate> coordinates = new ArrayList<>();

        final int sizeQuadrant = (int) Math.sqrt(size);
        final int rowStart = (row / sizeQuadrant) * sizeQuadrant;
        final int colStart = (col / sizeQuadrant) * sizeQuadrant;

        for (int i = 0; i < sizeQuadrant; i++)
            for (int j = 0; j < sizeQuadrant; j++)
                coordinates.add(Coordinate.create(rowStart + i, colStart + j));

        return coordinates;
    }

    public static List<Coordinate> createRowAndColFrom(final Coordinate coordinate, final int size) {
        final int row = coordinate.row();
        final int col = coordinate.column();

        return new java.util.ArrayList<>(IntStream.range(0, size)
                .mapToObj(i -> List.of(Coordinate.create(row, i), Coordinate.create(i, col)))
                .flatMap(List::stream)
                .toList());
    }
}