package com.github.mjaroslav.game.client.texture;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class TextureAtlas extends Texture {
    protected final int width, height;

    public TextureAtlas(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public TextureAtlas(int width, int height, @NotNull BufferedImage image) {
        super(image);
        this.width = width;
        this.height = height;
    }

    public TextureAtlas(int width, int height, @NotNull String location) {
        super(location);
        this.width = width;
        this.height = height;
    }

    public IconPos createIcon(int x, int y) {
        return new IconPos(x / (double) width, y / (double) height,
                (x + 1d) / width, (y + 1d) / height);
    }

    @Data
    public static class IconPos {
        public final double uMin, vMin, uMax, vMax;
    }
}
