package com.dan;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private Input input;
    private TetrisMap map;
    private final Player player = new Player(40, 40, Color.BLUE);
    private TetrisView view;
    double speed = 250;

    @Override
    public void start(Stage stage) {
        int w = 800;
        int h = 600;
        Canvas canvas = new Canvas(w, h);
        map = new TetrisMap(10, 10);
        view = new TetrisView(canvas, map);

        Scene scene = new Scene(new StackPane(canvas));
        input = new Input(scene);
        stage.setScene(scene);
        stage.setTitle("JavaFX 2D Game");
        stage.show();

//        map = new Map(canvas.getWidth(), canvas.getHeight());
//        player.setLocation(0, 0);
//        player.setSpeed(80, 80);

        new AnimationTimer() {
            final long updateFpsEvery = 500;
            final long frameEvery = (long) 4e8;
            long nextThreshold = 0;
            long fpsLast = 0;
            long frames = 0;
            long last = 0;
            String info = "";

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }

                double delta = now - last;
                double dt = delta / 1e9;
                last = now;
                if (frames % updateFpsEvery == 0) {
                    double fps = updateFpsEvery / ((now - fpsLast) / 1e9);
                    info = String.format("FPS: %s, X: %s, Y: %s", (int) fps, (int) player.getXLoc(), (int) player.getYLoc());
                    fpsLast = now;
                }

                if (now > nextThreshold) {
                    System.out.printf("Frame %s; now: %s; nextThreshold %s %n", frames, now, nextThreshold);
                    update(dt);
                    render(info);
                    frames += 1;
                    nextThreshold = now + frameEvery;
                }
            }
        }.start();
    }

    void update(double dt) {
        double x = 0;
        double y = 0;

        if (input.isPressed(KeyCode.W)) y -= speed * dt;
        if (input.isPressed(KeyCode.S)) y += speed * dt;
        if (input.isPressed(KeyCode.A)) x -= speed * dt;
        if (input.isPressed(KeyCode.D)) x += speed * dt;
//        player.update(dt, map);
        map.update();
//        player.move(x, y);
    }

    void render(String info) {
//        view.render(player, info);
        view.render(info);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
