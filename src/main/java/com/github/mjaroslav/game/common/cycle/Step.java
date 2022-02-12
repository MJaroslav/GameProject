package com.github.mjaroslav.game.common.cycle;

@FunctionalInterface
public interface Step {
    void update(double delta);
}
