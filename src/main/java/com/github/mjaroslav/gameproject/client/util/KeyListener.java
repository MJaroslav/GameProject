package com.github.mjaroslav.gameproject.client.util;

public interface KeyListener {
    int forKey();

    void onKeyPressed();

    void onKeyReleased();

    void onKeyRepeat(double time);
}
