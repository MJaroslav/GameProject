package com.github.mjaroslav.game.common.entity;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

public class EntityLiving extends Entity {
    @Getter
    protected float health;

    public EntityLiving(@NotNull Vector2d position, @NotNull Vector2d size) {
        super(position, size);
    }

    public void setHealth(float value) {
        health = value;
        if (health <= 0)
            setDead(true);
    }

    @Override
    public void step() {
        super.step();
    }
}
