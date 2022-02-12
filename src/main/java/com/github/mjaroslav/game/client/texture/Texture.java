package com.github.mjaroslav.game.client.texture;

import com.github.mjaroslav.game.Game;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    protected boolean isLoaded;
    protected int id;

    public Texture() {
        id = loadTexture(generatePlaceholder());
        isLoaded = false;
    }

    public Texture(@NotNull BufferedImage image) {
        id = loadTexture(image);
        isLoaded = true;
    }

    public Texture(@NotNull String location) {
        id = loadTexture(loadImage(location));
        isLoaded = true;
    }

    public void bind() {
        if (isLoaded)
            GL11.glBindTexture(GL_TEXTURE_2D, id);
    }

    public void dispose() {
        if (isLoaded) {
            glDeleteTextures(id);
            isLoaded = false;
        }
    }

    public static void unbindAll() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    private static int loadTexture(@NotNull BufferedImage image) {
        val pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        val buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (var y = 0; y < image.getHeight(); y++) {
            for (var x = 0; x < image.getWidth(); x++) {
                val pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureID;
    }

    private static BufferedImage generatePlaceholder() {
        val img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, 0xFF00FF);
        img.setRGB(1, 1, 0xFF00FF);
        img.setRGB(0, 1, 0x000000);
        img.setRGB(1, 0, 0x000000);
        return img;
    }

    private static BufferedImage loadImage(@NotNull String loc) {
        try {
            val resource = Game.class.getResource("/" + loc);
            if (resource != null)
                return ImageIO.read(resource);
        } catch (IOException ignored) {
        }
        return generatePlaceholder();
    }
}
