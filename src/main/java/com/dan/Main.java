package com.dan;

import com.dan.model.TetrisMap;
import com.dan.view.TetrisView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Input input;
    private TetrisMap map;
    private TetrisView view;

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
//                if (frames % updateFpsEvery == 0) {
//                    double fps = updateFpsEvery / ((now - fpsLast) / 1e9);
//                    info = String.format("FPS: %s, X: %s, Y: %s", (int) fps, (int) player.getXLoc(), (int) player.getYLoc());
//                    fpsLast = now;
//                }

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
        map.update(input.getPressedKeys());
    }

    void render(String info) {
        view.render(info);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
