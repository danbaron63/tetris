package com.dan;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TetrisView {

    private final GraphicsContext gc;
    private final double w;
    private final double h;
    private final TetrisMap map;
    private final double tileWidth;
    private final double tileHeight;

    public TetrisView(Canvas canvas, TetrisMap map) {
        this.gc = canvas.getGraphicsContext2D();
        this.w = canvas.getWidth();
        this.h = canvas.getHeight();
        this.map = map;
        this.tileWidth = w / map.getColumns();
        this.tileHeight = h / map.getRows();
    }

    public void render(String info) {
        // Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w,  h);

        // Player
        for (Coordinate coordinate : map.getAllTileCoordinates()) {
            gc.setFill(Color.YELLOW);
            gc.fillRect(
                    coordinate.x() * tileHeight, coordinate.y() * tileWidth,
                    tileHeight, tileWidth
            );
        }

        // Fps
        gc.setFill(Color.YELLOW);
        gc.fillText(info, 0, 600);
    }

}
