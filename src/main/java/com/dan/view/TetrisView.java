package com.dan.view;

import com.dan.model.Coordinate;
import com.dan.model.TetrisMap;
import com.dan.model.Tile;
import com.dan.model.TileState;
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

        // Tiles
        for (Tile tile : map.getAllTiles()) {
            Color color = switch (tile.state()) {
                case TileState.BREAKING -> Color.RED;
                case TileState.FALLING -> Color.GREEN;
                case TileState.NORMAL -> Color.YELLOW;
            };
            gc.setFill(color);
            gc.fillRect(
                    tile.x() * tileWidth, tile.y() * tileHeight,
                    tileWidth, tileHeight
            );
        }

        // Draw grid
        for (int r = 0; r < map.getRows(); r++) {
            gc.setStroke(Color.WHITE);
            gc.strokeLine(0, r * tileHeight, w, r * tileHeight);
        }
        for (int c = 0; c < map.getColumns(); c++) {
            gc.setStroke(Color.WHITE);
            gc.strokeLine(c * tileWidth, 0, c * tileWidth, h);
        }

        // Fps
        gc.setFill(Color.YELLOW);
        gc.fillText(info, 0, 600);
    }

}
