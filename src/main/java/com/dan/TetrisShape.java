package com.dan;

public enum TetrisShape {
    SQUARE(new boolean[][]{
            {true, true},
            {true, true}
    }),
    L_SHAPE(new boolean[][]{
            {true, false, false}, 
            {true, true, true}, 
            {false, false, false}
    }),
    LONG(new boolean[][]{
            {true, true, true}, 
            {false, false, false},
            {false, false, false}
    });

    private final boolean[][] tiles;

    TetrisShape(boolean[][] tiles) {
        this.tiles = tiles;
        if (tiles.length != tiles[0].length) {
            throw new RuntimeException(String.format("Tile should be square, it is %sx%s", tiles.length, tiles[0].length));
        }
        for (boolean[] row : tiles) {
            if (row.length > tiles.length) {
                throw new RuntimeException("Only supports 3x3 tiles");
            }
        }
    }

    static boolean[][] rotateCw(boolean[][] tiles) {
        /*
        0,0 | 0,1 | 0,2
        1,0 | 1,1 | 1,2
        2,0 | 2,1 | 2,2

        2,0 | 1,0 | 0,0
        2,1 | 1,1 | 0,1
        2,2 | 1,2 | 0,2
         */
        boolean[][] copy = new boolean[tiles.length][tiles.length];
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                copy[tiles.length - 1 - y][x] = tiles[x][y];
            }
        }
        return copy;
    }

    public boolean[][] getTiles(int rotate) {
        boolean[][] copy = new boolean[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            copy[i] = tiles[i].clone();
        }

        int rotations = rotate % 4;

        for (int r = 0; r < rotations; r++) {
            copy = rotateCw(copy);
        }

        return copy;
    }

    public int getSize() {
        return tiles.length;
    }
}
