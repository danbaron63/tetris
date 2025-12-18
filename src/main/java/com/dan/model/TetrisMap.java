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
    private final double updateFrequencyNanos;
    private double lastUpdate = 0;
    private final Input input;
    private double fallDist = 0;
    private double lastTs = 0;
    private int points = 0;
    private final Grid grid;

    public TetrisMap(int columns, int rows, Input input, double updateFrequencyNanos) {
        this.columns = columns;
        this.rows = rows;
        this.input = input;
        this.updateFrequencyNanos = updateFrequencyNanos;
        this.grid = new Grid(columns, rows);
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
            if (!grid.isRowEmpty(0)) {
                return GameState.LOST;
            }

            fallingObject = getNewObject();
            // Check if object can be placed as the grid may be full.
        } else {
            if (grid.canObjectMove(fallingObject)) {
                fallingObject.fallOne();
                if (pressedKeys.contains(KeyCode.A)) {
                    fallingObject.moveLeft(columns);
                }
                if (pressedKeys.contains(KeyCode.D)) {
                    fallingObject.moveRight(columns);
                }
            } else {
                fallingObject.getCoordinates().forEach(grid::setOccupied);
                objectAtBottom = true;
            }
        }
        final int clearedBlocks = grid.clearRows(getColumns(), getRows());
        points += clearedBlocks * 10;
        return GameState.RUNNING;
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
        for (int y = 0; y < grid.getHeight(); y++) {
            boolean[] row = grid.getRow(y);
            boolean fullRow = grid.isRowFull(y);
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
            boolean objectCanFall = grid.canObjectMove(fallingObject);
            tileList.add(new Tile(
                    coordinate.x(),
                    objectCanFall ? coordinate.y() + fallDist : coordinate.y(),
                    coordinate.state()
            ));
        }
        return tileList;
    }

    public int getPoints() {
        return points;
    }
}
