package com.github.mjaroslav.gameproject.common.block;

import com.github.mjaroslav.gameproject.client.texture.TextureAtlas;
import com.github.mjaroslav.gameproject.common.util.CircleCollision;
import com.github.mjaroslav.gameproject.common.world.Stage;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class Blocks {
    public static Block air = new BlockAir();
    public static Block wall = new BlockSolid() {
        @Override
        protected @NotNull TextureAtlas.IconPos createIcon(TextureAtlas blockAtlas) {
            return blockAtlas.createIcon(1, 0);
        }
    };
    public static Block test = new Block() {
        @Override
        public void takeCollisions(@NotNull List<Object> collisions, @NotNull Stage stage, @NotNull Vector2i pos) {
            collisions.add(new CircleCollision().setPositionAndRadius(pos.x + 0.5, pos.y + 0.5, 0.5));
        }
    };
}
