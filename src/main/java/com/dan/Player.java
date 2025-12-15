package com.dan;

import javafx.scene.paint.Color;

public class Player {
    private double xLoc;
    private double yLoc;
    private double h;
    private double w;
    private double xSpeed = 0;
    private double ySpeed = 0;
    private Direction xDir = Direction.LEFT;
    private Direction yDir = Direction.UP;
    private final Color color;

    public Player(double w, double h, Color color) {
        this.w = w;
        this.h = h;
        this.color = color;

        // Set start location to off-screen
        this.xLoc = -w;
        this.yLoc = -h;
    }

    public void setLocation(double x, double y) {
        this.xLoc = x;
        this.yLoc = y;
    }

    public double getXLoc() {
        return xLoc;
    }

    public double getYLoc() {
        return yLoc;
    }

    public void move(double x, double y) {
        this.xLoc += x;
        this.yLoc += y;
    }

    public Color getColor() {
        return color;
    }

    public double getHeight() {
        return h;
    }

    public double getWidth() {
        return w;
    }

    public void setSpeed(double x, double y) {
        this.xSpeed = x;
        this.ySpeed = y;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }

    public void update(double timeDiff, Map map) {
        double x = xSpeed * timeDiff;
        if (xDir == Direction.LEFT) {
            x = -x;
        } else {
            x = Math.abs(x);
        }
        double y = ySpeed * timeDiff;
        if (yDir == Direction.UP) {
            y = -y;
        } else {
            y = Math.abs(y);
        }
        move(x, y);

        // Collision detection with map border
        if (x >= map.getWidth()) {
            xDir = Direction.LEFT;
        } else if (x <= 0) {
            xDir = Direction.RIGHT;
        }

        if (y >= map.getHeight()) {
            yDir = Direction.UP;
        } else if (y <= 0) {
            yDir = Direction.DOWN;
        }
    }
}
