package com.dan.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class TetrisObjectTest {

    @Test
    void getCoordinates() {
        // Given
        TetrisObject object = new TetrisObject(1, TetrisShape.L_SHAPE, 0);

        // When
        List<Coordinate> coordinates = object.getCoordinates();

        // Then
        assertThat(coordinates).isEqualTo(List.of(
                new Coordinate(1, 0, TileState.NORMAL),
                new Coordinate(2, 0, TileState.NORMAL),
                new Coordinate(2, 1, TileState.NORMAL),
                new Coordinate(2, 2, TileState.NORMAL)
        ));
    }

    @Test
    void staticGetCoordinates() {
        // Given
        boolean[][] tiles = new boolean[][]{
                {true, false},
                {false, true}
        };
        int yOffset = 1;
        int xLoc = 4;
        int yLoc = 3;

        // When
        List<Coordinate> coordinates = TetrisObject.getCoordinates(tiles, 0, yOffset, xLoc, yLoc);

        // Then
        assertThat(coordinates).isEqualTo(List.of(
                new Coordinate(4, 4, TileState.NORMAL),
                new Coordinate(5, 5, TileState.NORMAL)
        ));
    }

    @Test
    void fallOne() {
        // Given
        TetrisObject object = new TetrisObject(5, TetrisShape.L_SHAPE, 0);

        // When
        object.fallOne();

        // Then
        assertThat(object.getXLoc()).isEqualTo(5);
        assertThat(object.getYLoc()).isEqualTo(1);
    }

    @Test
    void moveLeft() {
        // Given
        TetrisObject object = new TetrisObject(5, TetrisShape.L_SHAPE, 0);

        // When
        object.moveLeft(10);

        // Then
        assertThat(object.getXLoc()).isEqualTo(4);
        assertThat(object.getYLoc()).isEqualTo(0);
    }

    @Test
    void moveRight() {
        // Given
        TetrisObject object = new TetrisObject(5, TetrisShape.L_SHAPE, 0);

        // When
        object.moveRight(10);

        // Then
        assertThat(object.getXLoc()).isEqualTo(6);
        assertThat(object.getYLoc()).isEqualTo(0);
    }
}
