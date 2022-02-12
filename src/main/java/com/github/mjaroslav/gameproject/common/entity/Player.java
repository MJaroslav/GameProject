package com.github.mjaroslav.gameproject.common.entity;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

public class Player extends Entity {
    public Player(@NotNull Vector2d position, @NotNull Vector2d size) {
        super(position, size);
    }
}
