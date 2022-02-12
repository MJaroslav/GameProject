package com.github.mjaroslav.game.common.block;

import com.github.mjaroslav.game.client.render.TextureManager;
import com.github.mjaroslav.game.client.texture.TextureAtlas;
import com.github.mjaroslav.game.common.world.Stage;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public abstract class Block {
    protected TextureAtlas.IconPos pos;

    public TextureAtlas.IconPos getIcon() {
        if (pos == null)
            pos = createIcon(TextureManager.getInstance().getBlockAtlas());
        return pos;
    }

    @NotNull
    protected TextureAtlas.IconPos createIcon(TextureAtlas blockAtlas) {
        return blockAtlas.createIcon(0, 0);
    }

    public abstract void takeCollisions(@NotNull List<Object> collisions, @NotNull Stage stage, @NotNull Vector2i pos);
}
