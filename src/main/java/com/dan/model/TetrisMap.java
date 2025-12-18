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
    private double fallDist = 0;
    private double lastTs = 0;

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

    public GameState update(double ts) {
        GameState state = GameState.RUNNING;
        if (ts > lastUpdate + updateFrequencyNanos) {
            lastUpdate = ts;
            state = update(input.getPressedKeys());
            fallDist = 0;
        }
        double delta = ts - lastTs;
        fallDist += delta / updateFrequencyNanos;
        lastTs = ts;
        return state;
    }

    private GameState update(Set<KeyCode> pressedKeys) {
        if (newObjectRequired()) {
            // Check if in losing state.
            boolean[] topRow = tiles[0];
            if (!isRowEmpty(topRow)) {
                return GameState.LOST;
            }

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
        return GameState.RUNNING;
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

    public List<Tile> getAllTiles() {
        ArrayList<Tile> tileList = new ArrayList<>(3);
        for (int y = 0; y < tiles.length; y++) {
            boolean[] row = tiles[y];
            boolean fullRow = isRowFull(row);
            for (int x = 0; x < row.length; x++) {
                if (row[x]) {
                    if (fullRow) {
                        tileList.add(new Tile(x, y, TileState.BREAKING));
                    } else {
                        tileList.add(new Tile(x, y, TileState.NORMAL));
                    }
                }
            }
        }
        for (Coordinate coordinate : fallingObject.getCoordinates()) {
            boolean objectCanFall = canObjectMove();
            tileList.add(new Tile(
                    coordinate.x(),
                    objectCanFall ? coordinate.y() + fallDist : coordinate.y(),
                    coordinate.state()
            ));
        }
        return tileList;
    }

    public static void clearRows(boolean[][] tiles, int columns, int rows) {
        for (int y = 0; y < rows; y++) {
            if (isRowFull(tiles[y])) {
                // shift all values down 1
                System.out.printf("Row %s needs removing%n", y);
                removeRow(y, tiles, columns);
            }
        }
    }

    private static boolean isRowFull(boolean[] row) {
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

    private static boolean isRowEmpty(boolean[] row) {
        for (boolean tile : row) {
            if (tile) {
                return false;
            }
        }
        return true;
    }
}
