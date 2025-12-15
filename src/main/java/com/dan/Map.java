package com.dan;

public class Map {
    private final double h;
    private final double w;

    public Map(double w, double h) {
        this.h = h;
        this.w = w;
    }

    public double getHeight() {
        return h;
    }

    public double getWidth() {
        return w;
    }
}
