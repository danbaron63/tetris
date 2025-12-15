package com.dan;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class View {

    private final GraphicsContext gc;
    private final double h;
    private final double w;

    public View(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
        this.h = canvas.getHeight();
        this.w = canvas.getWidth();
    }

    public void render(Player player, String info) {
        // Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w,  h);

        // Player
        gc.setFill(player.getColor());
        gc.fillRect(
                player.getXLoc(), player.getYLoc(),
                player.getWidth(), player.getHeight()
        );

        // Fps
        gc.setFill(Color.YELLOW);
        gc.fillText(info, 0, 600);
    }

}
