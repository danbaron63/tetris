package com.dan.model;

import java.util.ArrayList;
import java.util.List;

public class TetrisObject {

    private int xLoc;
    private int yLoc;
    private final boolean[][] tiles;

    public TetrisObject(int startColumn, TetrisShape shape, int rotate) {
        this.xLoc = startColumn;
        this.yLoc = 0;
        this.tiles = shape.getTiles(rotate);
    }

    int getXLoc() {
        return xLoc;
    }

    int getYLoc() {
        return yLoc;
    }

    public void moveLeft(int columns) {
        moveX(-1, columns);
    }

    public
    void moveRight(int columns) {
        moveX(1, columns);
    }

    void moveX(int amount, int columns) {
        final List<Coordinate> coordinates = getCoordinates(tiles, amount, 0, getXLoc(), getYLoc());
        for (Coordinate coordinate : coordinates) {
            if (coordinate.x() < 0 || coordinate.x() >= columns) {
                return;
            }
        }
        this.xLoc += amount;
    }

    void fallOne() {
        this.yLoc += 1;
    }

    static List<Coordinate> getCoordinates(boolean[][] tiles, int xOffset, int yOffset, int xLoc, int yLoc) {
        ArrayList<Coordinate> coordinates = new ArrayList<>(3);
        for (int x = 0; x < tiles.length; x++) {
            boolean[] row = tiles[x];
            for (int y = 0; y < row.length; y++) {
                if (row[y]) {
                    coordinates.add(new Coordinate(xLoc + x + xOffset, yLoc + y + yOffset, TileState.FALLING));
                }
            }
        }
        return coordinates;
    }

    List<Coordinate> getCoordinatesOfNextFall() {
        return getCoordinates(tiles, 0, 1, getXLoc(), getYLoc());
    }

    List<Coordinate> getCoordinates() {
        return getCoordinates(tiles, 0, 0, getXLoc(), getYLoc());
    }
}
