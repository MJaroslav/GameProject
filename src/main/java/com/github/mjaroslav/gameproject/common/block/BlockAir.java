package com.github.mjaroslav.gameproject.common.block;

import com.github.mjaroslav.gameproject.common.world.Stage;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class BlockAir extends Block {

    @Override
    public void takeCollisions(@NotNull List<Object> collisions, @NotNull Stage stage, @NotNull Vector2i pos) {
    }
}
