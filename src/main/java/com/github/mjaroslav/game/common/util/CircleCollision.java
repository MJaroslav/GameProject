package com.github.mjaroslav.game.common.util;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

@Data
public class CircleCollision {
    final Vector2d position = new Vector2d();
    double radius = 1;

    public CircleCollision setPositionAndRadius(@NotNull Vector2d position, double radius) {
        this.radius = radius;
        this.position.set(position);
        return this;
    }

    public CircleCollision setPositionAndRadius(double x, double y, double radius) {
        this.radius = radius;
        position.set(x, y);
        return this;
    }

    public CircleCollision setPosition(@NotNull Vector2d position) {
        this.position.set(position);
        return this;
    }

    public CircleCollision setPosition(double x, double y) {
        position.set(x, y);
        return this;
    }

    public CircleCollision setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public CircleCollision resize(double additionalRadius) {
        this.radius += radius;
        return this;
    }

    public CircleCollision move(@NotNull Vector2d offset) {
        position.add(offset);
        return this;
    }

    public CircleCollision move(double x, double y) {
        position.add(x, y);
        return this;
    }
}
