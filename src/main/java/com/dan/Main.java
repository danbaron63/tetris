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

        Scene scene = new Scene(new StackPane(canvas));
        input = new Input(scene);
        map = new TetrisMap(10, 15, input, 4e8);
        view = new TetrisView(canvas, map);
        stage.setScene(scene);
        stage.setTitle("JavaFX 2D Game");
        stage.show();

        new AnimationTimer() {
            final long updateFpsEvery = 500;
            long fpsLast = 0;
            long frames = 0;
            String info = "";

            @Override
            public void handle(long now) {

                if (frames % updateFpsEvery == 0) {
                    double fps = updateFpsEvery / ((now - fpsLast) / 1e9);
                    info = String.format("FPS: %s", (int) fps);
                    fpsLast = now;
                }
                update(now);
                render(info);

                frames += 1;
            }
        }.start();
    }

    void update(double dt) {
        map.update(dt);
    }

    void render(String info) {
        view.render(info);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
