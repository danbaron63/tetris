package com.dan.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Grid implements Iterable<boolean[]> {

    private final int rows;
    private final boolean[][] tiles;

    Grid(int columns, int rows) {
        this.rows = rows;
        this.tiles = new boolean[rows][columns];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                tiles[y][x] = false;
            }
        }
    }

    int getHeight() {
        return tiles.length;
    }

    void setOccupied(Coordinate coordinate) {
        tiles[coordinate.y()][coordinate.x()] = true;
    }

    boolean[] getRow(final int row) {
        return tiles[row];
    }

    boolean canObjectMove(TetrisObject fallingObject) {
        List<Coordinate> nextCoordinates = fallingObject.getCoordinatesOfNextFall();
        for (Coordinate coordinate : nextCoordinates) {
            System.out.printf("Coordinate: x = %s, y = %s%n", coordinate.x(), coordinate.y());

            // if tile occupied or below bottom.
            if (!(coordinate.y() < rows) || tiles[coordinate.y()][coordinate.x()]) {
                return false;
            }
        }
        return true;
    }

    boolean isRowFull(final int row) {
        boolean result = true;
        for (boolean b : getRow(row)) {
            result = result && b;
        }
        return result;
    }

    private static void removeRow(int rowToRemove,
                                  boolean[][] tiles,
                                  int columns) {
        // Shift everything above the removed row down
        for (int y = rowToRemove; y > 0; y--) {
            if (columns >= 0) System.arraycopy(tiles[y - 1], 0, tiles[y], 0, columns);
        }
        // Clear the top row
        for (int x = 0; x < columns; x++) {
            tiles[0][x] = false;
        }
    }

    boolean isRowEmpty(int row) {
        for (boolean tile : getRow(row)) {
            if (tile) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<boolean[]> iterator() {
        return Arrays.stream(tiles).iterator();
    }

    public int clearRows(int columns, int rows) {
        int clearedBlocks = 0;
        for (int y = 0; y < rows; y++) {
            if (isRowFull(y)) {
                // shift all values down 1
                System.out.printf("Row %s needs removing%n", y);
                removeRow(y, tiles, columns);
                clearedBlocks += columns;
            }
        }
        return clearedBlocks;
    }
}
