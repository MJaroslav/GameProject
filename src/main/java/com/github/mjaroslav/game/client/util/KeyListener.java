package com.github.mjaroslav.game.client.util;

public interface KeyListener {
    int forKey();

    void onKeyPressed();

    void onKeyReleased();

    void onKeyRepeat(double time);
}
