package com.github.mjaroslav.game.common.cycle;

@FunctionalInterface
public interface Loop {
    default void init() {
    }

    default void beforeUpdate(double delta) {
    }

    default void afterUpdate(double delta) {
    }

    void update(double delta);

    default void destroy() {
    }
}
