package com.github.mjaroslav.gameproject.client.gui.world;

import com.github.mjaroslav.gameproject.client.render.TextureManager;
import com.github.mjaroslav.gameproject.client.texture.Texture;
import com.github.mjaroslav.gameproject.client.util.GLStateManager;
import com.github.mjaroslav.gameproject.common.block.Block;
import com.github.mjaroslav.gameproject.common.entity.Entity;
import com.github.mjaroslav.gameproject.common.world.World;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.*;

@RequiredArgsConstructor
public class WorldRenderer {
    @NotNull
    public final World world;
    public final double zoom;

    public void draw(double partial) {
        val STATE_TEXTURE_2D = GLStateManager.enable(GL_TEXTURE_2D);
        TextureManager.getInstance().getBlockAtlas().bind();
        glColor4d(1, 1, 1, 1);
        glScaled(zoom, zoom, zoom);
        val STATE_BLEND = GLStateManager.enable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        val stage = world.getCurrentStage();
        glBegin(GL_QUADS);
        if (stage != null)
            for (var y = 0; y < stage.getHeight(); y++)
                for (var x = 0; x < stage.getWidth(); x++)
                    drawBlock(stage.getBlock(new Vector2i(x, y)), x, y);
        glEnd();
        if (stage != null)
            stage.getEntities().forEach(entity -> drawEntity(entity, partial));
        GLStateManager.set(GL_BLEND, STATE_BLEND);
        GLStateManager.set(GL_TEXTURE_2D, STATE_TEXTURE_2D);
        Texture.unbindAll();
    }

    protected void drawEntity(@NotNull Entity entity, double partial) {
        glPushMatrix();
        val texture = EntityRenderer.getInstance().getTextureForEntity(entity);
        if (texture == null)
            Texture.unbindAll();
        else
            texture.bind();

        val pos = new Vector2d(entity.getPrevPosition()).lerp(entity.getPosition(), partial);
        glBegin(GL_QUADS);
        glTexCoord2d(1, 1);
        glVertex2d((pos.x + entity.getSize().x / 2) , (pos.y + entity.getSize().y / 2) );

        glTexCoord2d(1, 0);
        glVertex2d((pos.x + entity.getSize().x / 2) , (pos.y - entity.getSize().y / 2) );

        glTexCoord2d(0, 0);
        glVertex2d((pos.x - entity.getSize().x / 2) , (pos.y - entity.getSize().y / 2));

        glTexCoord2d(0, 1);
        glVertex2d((pos.x - entity.getSize().x / 2) , (pos.y + entity.getSize().y / 2));
        glEnd();
        glPopMatrix();
    }

    protected void drawBlock(@NotNull Block block, int x, int y) {
        val icon = block.getIcon();
        glTexCoord2d(icon.uMax, icon.vMax);
        glVertex2d((x + 1) , (y + 1) );

        glTexCoord2d(icon.uMax, icon.vMin);
        glVertex2d((x + 1), y );

        glTexCoord2d(icon.uMin, icon.vMin);
        glVertex2d(x , y );

        glTexCoord2d(icon.uMin, icon.vMax);
        glVertex2d(x, (y + 1));

    }
}
