package com.dan;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Input {

    private final Set<KeyCode> keys = new HashSet<>();

    public Input(Scene scene) {
        scene.setOnKeyPressed(e -> keys.add(e.getCode()));
        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));
    }

    boolean isPressed(KeyCode keyCode) {
        return keys.contains(keyCode);
    }
}
