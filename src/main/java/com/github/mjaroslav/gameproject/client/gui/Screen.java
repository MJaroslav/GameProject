package com.github.mjaroslav.gameproject.client.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    private final List<Element> elements = new ArrayList<>();

    public abstract void draw(double partial);
}
