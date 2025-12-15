package com.dan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class TetrisShapeTest {

    @Test
    void squareNoRotation() {
        TetrisShape object = TetrisShape.SQUARE;

        boolean[][] expected = {
                {true, true},
                {true, true}
        };

        boolean[][] result = object.getTiles(0);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void rotatesSquareMatrixClockwise() {
        TetrisShape object = TetrisShape.SQUARE;

        boolean[][] expected = {
                { true, true },
                { true, true }
        };

        boolean[][] result = object.getTiles(1);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2",
            "6",
            "10"
    })
    void rotatesSquareMatrixClockwiseTwice(int rotate) {
        TetrisShape object = TetrisShape.SQUARE;

        boolean[][] expected = {
                { true, true },
                { true, true }
        };

        boolean[][] result = object.getTiles(rotate);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void lShapeNoRotation() {
        TetrisShape object = TetrisShape.L_SHAPE;

        boolean[][] expected = {
                {true, false, false},
                {true, true, true},
                {false, false, false}
        };

        boolean[][] result = object.getTiles(0);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void rotatesLShapeMatrixClockwise() {
        TetrisShape object = TetrisShape.L_SHAPE;

        boolean[][] expected = {
                { false, true, false },
                { false, true, false },
                { true, true, false }
        };

        boolean[][] result = object.getTiles(1);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void longNoRotation() {
        TetrisShape object = TetrisShape.LONG;

        boolean[][] expected = {
                {true, true, true},
                {false, false, false},
                {false, false, false}
        };

        boolean[][] result = object.getTiles(0);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void rotatesLongMatrixClockwise() {
        TetrisShape object = TetrisShape.LONG;

        boolean[][] expected = {
                { true, false, false },
                { true, false, false },
                { true, false, false }
        };

        boolean[][] result = object.getTiles(1);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "SQUARE,2",
            "L_SHAPE,3",
            "LONG,3"
    })
    void getSize(TetrisShape shape, int size) {
        // When
        int actualSize = shape.getSize();

        // Then
        assertThat(actualSize).isEqualTo(size);
    }
}
