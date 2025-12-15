package com.dan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TetrisMap {

    private final int columns;
    private final int rows;
    private TetrisObject fallingObject;
    private boolean objectAtBottom = true;
    private final boolean[][] tiles;

    public TetrisMap(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.tiles = new boolean[columns][rows];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                tiles[x][y] = false;
            }
        }
    }

    void update() {
        if (newObjectRequired()) {
            fallingObject = getNewObject();
            System.out.printf("X = %s, Y = %s%n", fallingObject.getXLoc(), fallingObject.getYLoc());
        } else {
            if (canObjectMove()) {
                fallingObject.fallOne();
            } else {
                for (Coordinate coordinate : fallingObject.getCoordinates()) {
                    tiles[coordinate.x()][coordinate.y()] = true;
                }
                objectAtBottom = true;
            }
        }
    }

    private boolean canObjectMove() {
        List<Coordinate> nextCoordinates = fallingObject.getCoordinatesOfNextFall();
        for (Coordinate coordinate : nextCoordinates) {
            System.out.printf("Coordinate: x = %s, y = %s%n", coordinate.x(), coordinate.y());

            // if tile occupied or below bottom.
            if (coordinate.y() >= rows - 1 || tiles[coordinate.x()][coordinate.y()]) {
                return false;
            }
        }
        return true;
    }

    private boolean newObjectRequired() {
        if (objectAtBottom) {
            objectAtBottom = false;
            return true;
        }
        return false;
    }

    TetrisObject getNewObject() {
        final TetrisShape shape = switch (ThreadLocalRandom.current().nextInt(1, 4)) {
            case 1 -> TetrisShape.SQUARE;
            case 2 -> TetrisShape.L_SHAPE;
            case 3 -> TetrisShape.LONG;
            default -> throw new RuntimeException("Random number out of range");
        };
        final int column = ThreadLocalRandom.current().nextInt(0, columns);
        final int rotate = ThreadLocalRandom.current().nextInt();
        return new TetrisObject(column, shape, rotate);
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    List<Coordinate> getAllTileCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>(3);
        for (int x = 0; x < tiles.length; x++) {
            boolean[] row = tiles[x];
            for (int y = 0; y < row.length; y++) {
                if (row[y]) {
                    coordinates.add(new Coordinate(x, y));
                }
            }
        }
        coordinates.addAll(fallingObject.getCoordinates());
        return coordinates;
    }
}
