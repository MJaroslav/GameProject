package com.github.mjaroslav.game.client.render;

import com.github.mjaroslav.game.client.texture.Texture;
import com.github.mjaroslav.game.client.texture.TextureAtlas;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextureManager {
    private static TextureManager instance;

    public static TextureManager getInstance() {
        if (instance == null)
            instance = new TextureManager();
        return instance;
    }

    private final Map<String, Texture> cachedTextures = new HashMap<>();

    public Texture getTexture(@NotNull String path) {
        if (cachedTextures.containsKey(path))
            return cachedTextures.get(path);
        val result = new Texture(path);
        cachedTextures.put(path, result);
        return result;
    }

    public TextureAtlas getAtlas(@NotNull String path, int width, int height) {
        if (cachedTextures.containsKey(path))
            return (TextureAtlas) cachedTextures.get(path);
        val result = new TextureAtlas(width, height, path);
        cachedTextures.put(path, result);
        return result;
    }

    public TextureAtlas getBlockAtlas() {
        return getAtlas("block_atlas.png", 8, 8);
    }

    public void dispose() {
        cachedTextures.values().forEach(Texture::dispose);
        cachedTextures.clear();
    }
}
