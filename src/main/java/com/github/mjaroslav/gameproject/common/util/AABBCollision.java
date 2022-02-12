package com.github.mjaroslav.gameproject.common.util;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

@Data
public class AABBCollision {
    final Vector2d position = new Vector2d();
    final Vector2d halfSize = new Vector2d();

    public AABBCollision setPositionAndHalfSize(@NotNull Vector2d position, @NotNull Vector2d halfSize) {
        this.position.set(position);
        this.halfSize.set(halfSize);
        return this;
    }

    public AABBCollision setPositionAndHalfSize(double x, double y, double halfWidth, double halfHeight) {
        position.set(x, y);
        halfSize.set(halfWidth, halfHeight);
        return this;
    }

    public AABBCollision setHalfSize(@NotNull Vector2d halfSize) {
        this.halfSize.set(halfSize);
        return this;
    }

    public AABBCollision setHalfSize(double width, double height) {
        halfSize.set(width, height);
        return this;
    }

    public AABBCollision setPosition(@NotNull Vector2d position) {
        this.position.set(position);
        return this;
    }

    public AABBCollision setPosition(double x, double y) {
        position.set(x, y);
        return this;
    }

    public AABBCollision resize(@NotNull Vector2d additionHalfSize) {
        halfSize.add(additionHalfSize);
        return this;
    }

    public AABBCollision resize(double additionalHalfWidth, double additionalHalfHeight) {
        halfSize.add(additionalHalfWidth, additionalHalfHeight);
        return this;
    }

    public AABBCollision move(@NotNull Vector2d offset) {
        position.add(offset);
        return this;
    }

    public AABBCollision move(double x, double y) {
        position.add(x, y);
        return this;
    }
}
