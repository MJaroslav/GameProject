package com.github.mjaroslav.gameproject.common.cycle;

@FunctionalInterface
public interface Step {
    void update(double delta);
}
