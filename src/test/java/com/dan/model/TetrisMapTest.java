package com.dan.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class TetrisMapTest {

    private final TetrisMap map = new TetrisMap(10, 15);

    @Test
    void getNewObject() {
        // When
        TetrisObject object = map.getNewObject();

        // Then
        assertThat(object.getYLoc()).isEqualTo(0);
        assertThat(object.getXLoc()).isBetween(0, 10);
    }

    @Test
    void getRows() {
        // When
        int rows = map.getRows();

        // Then
        assertThat(rows).isEqualTo(15);
    }

    @Test
    void getColumns() {
        // When
        int cols = map.getColumns();

        // Then
        assertThat(cols).isEqualTo(10);
    }

    @Test
    void clearRows() {
        // Given
        boolean[][] inputRows = new boolean[][] {
                {true, true,  true },
                {false, false, true },
                {false, false, true }
        };
        boolean[][] expectedOutput = new boolean[][] {
                {false, true, true },
                {false, false, false},
                {false, false, false}
        };

        // When
        TetrisMap.clearRows(inputRows, 3, 3);

        // Then
        assertThat(inputRows).isDeepEqualTo(expectedOutput);
    }
}
