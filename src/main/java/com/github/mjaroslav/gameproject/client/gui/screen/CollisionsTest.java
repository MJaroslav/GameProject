package com.github.mjaroslav.gameproject.client.gui.screen;

import com.github.mjaroslav.gameproject.client.gui.Screen;
import com.github.mjaroslav.gameproject.common.util.AABBCollision;
import com.github.mjaroslav.gameproject.common.util.CircleCollision;
import com.github.mjaroslav.gameproject.common.util.Collisions;
import com.github.mjaroslav.gameproject.client.util.InputHandler;
import lombok.val;
import lombok.var;
import org.lwjgl.glfw.GLFW;

import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;

public class CollisionsTest extends Screen {
    final AABBCollision a = new AABBCollision().setPositionAndHalfSize(1.5, 1.5, 0.5, 0.5);
    final AABBCollision b = new AABBCollision().setPositionAndHalfSize(3.5, 3.5, 0.5, 1);
    final CircleCollision c = new CircleCollision().setPositionAndRadius(1.5, 1.5, 0.5);
    final CircleCollision d = new CircleCollision().setPositionAndRadius(4, 1, 1);
    final double scale = 100;

    @Override
    public void draw(double partial) {
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            if (InputHandler.isShiftPressed())
                c.move(0, 0.01);
            else
                a.move(0, 0.01);
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_UP))
            if (InputHandler.isShiftPressed())
                c.move(0, -0.01);
            else
                a.move(0, -0.01);
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT))
            if (InputHandler.isShiftPressed())
                c.move(-0.01, 0);
            else
                a.move(-0.01, 0);
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
            if (InputHandler.isShiftPressed())
                c.move(0.01, 0);
            else
                a.move(0.01, 0);
        glPushMatrix();
        glScaled(scale, scale, scale);
        glTranslated(1, 1, 0);
        glPushMatrix();

        var isCollided = Collisions.auto(a, b).isCollided() || Collisions.auto(a, c).isCollided()
                || Collisions.auto(a, d).isCollided();

        if (isCollided)
            glLineWidth(3);
        else
            glLineWidth(1);

        drawAABB(a);
        glLineWidth(1);
        glPopMatrix();
        glPushMatrix();

        isCollided = Collisions.auto(c, a).isCollided() || Collisions.auto(c, b).isCollided()
                || Collisions.auto(c, d).isCollided();

        if (isCollided)
            glLineWidth(3);
        else
            glLineWidth(1);

        drawCircle(c);
        glLineWidth(1);
        glPopMatrix();
        glPushMatrix();
        drawAABB(b);
        glPopMatrix();
        glPushMatrix();
        drawCircle(d);
        glPopMatrix();
        drawCollisions();
        glPopMatrix();

    }

    private void drawCollisions() {
        glPushMatrix();
        drawCollision(a, b);
        glPopMatrix();
        glPushMatrix();
        drawCollision(a, c);
        glPopMatrix();
        glPushMatrix();
        drawCollision(a, d);
        glPopMatrix();

        glPushMatrix();
        drawCollision(b, a);
        glPopMatrix();
        glPushMatrix();
        drawCollision(b, c);
        glPopMatrix();
        glPushMatrix();
        drawCollision(b, d);
        glPopMatrix();

        glPushMatrix();
        drawCollision(c, a);
        glPopMatrix();
        glPushMatrix();
        drawCollision(c, b);
        glPopMatrix();
        glPushMatrix();
        drawCollision(c, d);
        glPopMatrix();

        glPushMatrix();
        drawCollision(d, a);
        glPopMatrix();
        glPushMatrix();
        drawCollision(d, b);
        glPopMatrix();
        glPushMatrix();
        drawCollision(d, c);
        glPopMatrix();
    }

    private void drawCollision(AABBCollision a, Object b) {
        val m = Collisions.auto(a, b);
        if (m.isCollided()) {
            glBegin(GL_LINE_LOOP);
            m.getNormal().mul(m.getPenetration());
            glVertex2d(a.getPosition().x, a.getPosition().y);
            glVertex2d(a.getPosition().x + m.getNormal().x, a.getPosition().y + m.getNormal().y);
            glEnd();
        }
    }

    private void drawCollision(CircleCollision a, Object b) {
        val m = Collisions.auto(a, b);
        if (m.isCollided()) {
            glBegin(GL_LINE_LOOP);
            m.getNormal().mul(m.getPenetration());
            glVertex2d(a.getPosition().x, a.getPosition().y);
            glVertex2d(a.getPosition().x + m.getNormal().x, a.getPosition().y + m.getNormal().y);
            glEnd();
        }
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
