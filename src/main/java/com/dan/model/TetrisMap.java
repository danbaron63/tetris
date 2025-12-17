package com.dan.model;

import com.dan.Input;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TetrisMap {
    /**
     * Tetris map implemented by a 2d boolean array representing tiles.
     * Array should be organised in rows, e.g. tiles[row#][col#].
     * This allows easy iterating over rows rather than columns.
     */

    private final int columns;
    private final int rows;
    private TetrisObject fallingObject;
    private boolean objectAtBottom = true;
    private final boolean[][] tiles;
    private final double updateFrequencyNanos;
    private double lastUpdate = 0;
    private final Input input;

    public TetrisMap(int columns, int rows, Input input, double updateFrequencyNanos) {
        this.columns = columns;
        this.rows = rows;
        this.input = input;
        this.updateFrequencyNanos = updateFrequencyNanos;
        this.tiles = new boolean[rows][columns];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                tiles[y][x] = false;
            }
        }
    }

    public void update(double ts) {
        if (ts > lastUpdate + updateFrequencyNanos) {
            lastUpdate = ts;
            update(input.getPressedKeys());
        }
    }

    private void update(Set<KeyCode> pressedKeys) {
        if (newObjectRequired()) {
            fallingObject = getNewObject();
            // Check if object can be placed as the grid may be full.
        } else {
            if (canObjectMove()) {
                fallingObject.fallOne();
                if (pressedKeys.contains(KeyCode.A)) {
                    fallingObject.moveLeft(columns);
                }
                if (pressedKeys.contains(KeyCode.D)) {
                    fallingObject.moveRight(columns);
                }
            } else {
                for (Coordinate coordinate : fallingObject.getCoordinates()) {
                    tiles[coordinate.y()][coordinate.x()] = true;
                }
                objectAtBottom = true;
            }
        }
        clearRows(tiles, getColumns(), getRows());
    }

    private boolean canObjectMove() {
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

    private boolean newObjectRequired() {
        if (objectAtBottom) {
            objectAtBottom = false;
            return true;
        }
        return false;
    }

    public TetrisObject getNewObject() {
        final TetrisShape shape = switch (ThreadLocalRandom.current().nextInt(1, 4)) {
            case 1 -> TetrisShape.SQUARE;
            case 2 -> TetrisShape.L_SHAPE;
            case 3 -> TetrisShape.LONG;
            default -> throw new RuntimeException("Random number out of range");
        };
        final int column = ThreadLocalRandom.current().nextInt(0, columns - shape.getSize() + 1);
        final int rotate = ThreadLocalRandom.current().nextInt();
        return new TetrisObject(column, shape, rotate);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Coordinate> getAllTileCoordinates() {
        return getAllTileCoordinates(tiles, fallingObject);
    }

    public static List<Coordinate> getAllTileCoordinates(boolean[][] tiles, TetrisObject fallingObject) {
        ArrayList<Coordinate> coordinates = new ArrayList<>(3);
        for (int y = 0; y < tiles.length; y++) {
            boolean[] row = tiles[y];
            boolean fullRow = isFullRow(row);
            for (int x = 0; x < row.length; x++) {
                if (row[x]) {
                    if (fullRow) {
                        coordinates.add(new Coordinate(x, y, TileState.BREAKING));
                    } else {
                        coordinates.add(new Coordinate(x, y, TileState.NORMAL));
                    }
                }
            }
        }
        coordinates.addAll(fallingObject.getCoordinates());
        return coordinates;
    }

    public static void clearRows(boolean[][] tiles, int columns, int rows) {
        for (int y = 0; y < rows; y++) {
            if (isFullRow(tiles[y])) {
                // shift all values down 1
                System.out.printf("Row %s needs removing%n", y);
                removeRow(y, tiles, columns);
            }
        }
    }

    private static boolean isFullRow(boolean[] row) {
        boolean result = true;
        for (boolean b : row) {
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
}
