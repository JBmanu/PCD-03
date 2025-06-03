package view.utils;

import model.Coordinate;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class GridUtils {

    public static Border getCellBorder(final int row, final int col, final int gridSize,
                                       final int thin, final int thick, final Color color) {
        final int quadrantSize = (int) Math.sqrt(gridSize);
        int top = (row % quadrantSize == 0 && row != 0) ? thick : thin;
        int left = (col % quadrantSize == 0 && col != 0) ? thick : thin;
        int bottom = ((row + 1) % quadrantSize == 0 && row != gridSize - 1) ? thick : thin;
        int right = ((col + 1) % quadrantSize == 0 && col != gridSize - 1) ? thick : thin;
        if (row == 0) top = 0;
        if (col == 0) left = 0;
        if (row == gridSize - 1) bottom = 0;
        if (col == gridSize - 1) right = 0;

        return BorderFactory.createMatteBorder(top, left, bottom, right, color);
    }

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