package com.github.mjaroslav.gameproject.common.block;

import com.github.mjaroslav.gameproject.common.util.AABBCollision;
import com.github.mjaroslav.gameproject.common.world.Stage;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class BlockSolid extends Block {
    @Override
    public void takeCollisions(@NotNull List<Object> collisions, @NotNull Stage stage, @NotNull Vector2i pos) {
        collisions.add(new AABBCollision().setPositionAndHalfSize(pos.x + 0.5d, pos.y + 0.5d, 0.5d, 0.5d));
    }
}
