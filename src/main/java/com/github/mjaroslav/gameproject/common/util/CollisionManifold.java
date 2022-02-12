package com.github.mjaroslav.gameproject.common.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

@Data
public class CollisionManifold {
    @NotNull
    final Object first;
    @NotNull
    final Object second;
    @Setter(AccessLevel.NONE)
    double penetration;
    @Setter(AccessLevel.NONE)
    boolean collided;
    @NotNull
    final Vector2d normal = new Vector2d();
}
