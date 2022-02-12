package com.github.mjaroslav.game.client.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Element {
    public final int ID;
    protected int width, height, x, y;

    public abstract void draw(float partial);
}
