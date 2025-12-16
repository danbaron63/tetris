package com.dan.model;

import com.dan.Input;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TetrisMap {

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
        this.tiles = new boolean[columns][rows];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                tiles[x][y] = false;
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
                    tiles[coordinate.x()][coordinate.y()] = true;
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
            if (!(coordinate.y() < rows) || tiles[coordinate.x()][coordinate.y()]) {
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
        final int column = ThreadLocalRandom.current().nextInt(0, columns - shape.getSize());
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
        ArrayList<Coordinate> coordinates = new ArrayList<>(3);
        for (int x = 0; x < tiles.length; x++) {
            boolean[] row = tiles[x];
            for (int y = 0; y < row.length; y++) {
                if (row[y]) {
                    coordinates.add(new Coordinate(x, y, TileState.NORMAL));
                }
            }
        }
        coordinates.addAll(fallingObject.getCoordinates());
        return coordinates;
    }

    public static void clearRows(boolean[][] tiles, int columns, int rows) {
        for (int y = 0; y < rows; y++) {
            boolean result = true;
            for (int x = 0; x < columns; x++) {
                result = result && tiles[x][y];
            }
            if (result) {
                // shift all values down 1
                System.out.printf("Row %s needs removing%n", y);
                removeRow(y, tiles, columns);
            }
        }
    }

    private static void removeRow(int rowToRemove,
                                  boolean[][] tiles,
                                  int columns) {

        // Shift everything above the removed row down
        for (int y = rowToRemove; y > 0; y--) {
            for (int x = 0; x < columns; x++) {
                tiles[x][y] = tiles[x][y - 1];
            }
        }

        // Clear the top row
        for (int x = 0; x < columns; x++) {
            tiles[x][0] = false;
        }
    }
}
