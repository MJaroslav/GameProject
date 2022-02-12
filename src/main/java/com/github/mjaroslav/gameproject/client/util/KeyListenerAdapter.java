package com.github.mjaroslav.gameproject.client.util;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class KeyListenerAdapter implements KeyListener {
    private int key;

    @Override
    public int forKey() {
        return key;
    }

    @Override
    public void onKeyPressed() {
    }

    @Override
    public void onKeyReleased() {
    }

    @Override
    public void onKeyRepeat(double time) {
    }
}
