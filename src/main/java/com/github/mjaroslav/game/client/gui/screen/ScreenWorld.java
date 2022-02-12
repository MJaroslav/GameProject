package com.github.mjaroslav.game.client.gui.screen;

import com.github.mjaroslav.game.client.ClientGame;
import com.github.mjaroslav.game.client.gui.Screen;
import com.github.mjaroslav.game.client.gui.world.WorldRenderer;
import com.github.mjaroslav.game.client.util.GLStateManager;
import com.github.mjaroslav.game.common.util.AABBCollision;
import com.github.mjaroslav.game.common.util.CircleCollision;
import com.github.mjaroslav.game.common.util.GameStates;
import com.github.mjaroslav.game.common.world.Stage;
import org.lwjgl.opengl.GL11;

import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;

public class ScreenWorld extends Screen {
    public final WorldRenderer renderer;

    public ScreenWorld() {
        renderer = new WorldRenderer(ClientGame.getUpdateLoop().getWorld(), 120);
    }

    public boolean isPressed = false;

    @Override
    public void draw(double partial) {
        GL11.glPushMatrix();

        GL11.glPushMatrix();
        renderer.draw(partial);
        GL11.glPopMatrix();

        GL11.glScaled(renderer.zoom, renderer.zoom, renderer.zoom);

        glPushMatrix();
        if (GameStates.debugDrawing) drawStageCollisions(ClientGame.getUpdateLoop().getWorld().getCurrentStage());
        glPopMatrix();

        glColor3d(1, 0, 1);

        GLStateManager.enable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

//        glPushMatrix();
//        glScaled(1 / renderer.zoom, 1 / renderer.zoom, 1 / renderer.zoom);
//        glPopMatrix();
        GL11.glPopMatrix();
    }

    private void drawStageCollisions(Stage stage) {
        if (stage == null)
            return;
        GL11.glColor4d(0, 1, 0, 1);
        stage.collectCollisionsFromAll().forEach(collision -> {
            if (collision instanceof AABBCollision) {
                glPushMatrix();
                drawAABB((AABBCollision) collision);
                glPopMatrix();
            }
            if (collision instanceof CircleCollision) {
                glPushMatrix();
                drawCircle((CircleCollision) collision);
                glPopMatrix();
            }
        });
        GL11.glColor4d(1, 0, 0, 1);
        stage.getAllCollisions().forEach(m -> {
            glPushMatrix();
            if (m.getFirst() instanceof AABBCollision) {
                glPushMatrix();
                drawAABB((AABBCollision) m.getFirst());
                glPopMatrix();
                glPushMatrix();
                AABBCollision a = (AABBCollision) m.getFirst();
                m.getNormal().mul(m.getPenetration());
                glVertex2d(a.getPosition().x, a.getPosition().y);
                glVertex2d(a.getPosition().x + m.getNormal().x, a.getPosition().y + m.getNormal().y);
                glPopMatrix();
            }
            if (m.getFirst() instanceof CircleCollision) {
                glPushMatrix();
                drawCircle((CircleCollision) m.getFirst());
                glPopMatrix();
                glPushMatrix();
                CircleCollision a = (CircleCollision) m.getFirst();
                m.getNormal().mul(m.getPenetration());
                glVertex2d(a.getPosition().x, a.getPosition().y);
                glVertex2d(a.getPosition().x + m.getNormal().x, a.getPosition().y + m.getNormal().y);
                glPopMatrix();
            }
            glPopMatrix();
        });
    }

    private void drawAABB(AABBCollision aabb) {
        glBegin(GL_LINE_LOOP);
        glVertex2d(aabb.getPosition().x - aabb.getHalfSize().x, aabb.getPosition().y - aabb.getHalfSize().y);
        glVertex2d(aabb.getPosition().x + aabb.getHalfSize().x, aabb.getPosition().y - aabb.getHalfSize().y);
        glVertex2d(aabb.getPosition().x + aabb.getHalfSize().x, aabb.getPosition().y + aabb.getHalfSize().y);
        glVertex2d(aabb.getPosition().x - aabb.getHalfSize().x, aabb.getPosition().y + aabb.getHalfSize().y);
        glEnd();
    }

    private void drawCircle(CircleCollision circle) {
        int i;
        int lineAmount = 100; //# of triangles used to draw circle

        //GLfloat radius = 0.8f; //radius
        double twicePi = 2.0f * PI;

        glBegin(GL_LINE_LOOP);
        for (i = 0; i <= lineAmount; i++)
            glVertex2d(circle.getPosition().x + (circle.getRadius() * Math.cos(i * twicePi / lineAmount)),
                    circle.getPosition().y + (circle.getRadius() * Math.sin(i * twicePi / lineAmount))
            );
        glEnd();
    }
}
