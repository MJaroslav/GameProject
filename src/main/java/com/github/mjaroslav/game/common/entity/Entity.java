package com.github.mjaroslav.game.common.entity;

import com.github.mjaroslav.game.client.ClientGame;
import com.github.mjaroslav.game.client.util.InputHandler;
import com.github.mjaroslav.game.common.util.AABBCollision;
import com.github.mjaroslav.game.common.util.GameStates;
import com.github.mjaroslav.game.common.world.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class Entity {
    protected Stage stage;

    @NotNull
    protected final Vector2d position, size;

    protected final Vector2d prevPosition = new Vector2d(),
            velocity = new Vector2d(), prevVelocity = new Vector2d();

    protected boolean gravity = false;
    protected boolean noClip;
    protected boolean dead;

    protected boolean pushedRightWall;
    protected boolean pushesRightWall;

    protected boolean pushedLeftWall;
    protected boolean pushesLeftWall;

    protected boolean wasOnGround;
    protected boolean onGround;

    protected boolean wasAtCeiling;
    protected boolean atCeiling;


    public void takeCollisions(@NotNull List<Object> collisions) {
        takeSensorCollisions(collisions, 0);
        takeSensorCollisions(collisions, 1);
        takeSensorCollisions(collisions, 2);
        takeSensorCollisions(collisions, 3);
    }

    public void takeSensorCollisions(@NotNull List<Object> collisions, int side) {
        switch (side) {
            case 0:
                collisions.add(new AABBCollision().setPosition(position.x - size.x / 2, position.y).setHalfSize(0.05d, size.y / 2d - 0.05d));
                break;
            case 1:
                collisions.add(new AABBCollision().setPosition(position.x + size.x / 2, position.y).setHalfSize(0.05d, size.y / 2d - 0.05d));
                break;
            case 2:
                collisions.add(new AABBCollision().setPosition(position.x, position.y - size.y / 2).setHalfSize(size.x / 2d - 0.05d, 0.05d));
                break;
            case 3:
                collisions.add(new AABBCollision().setPosition(position.x, position.y + size.y / 2).setHalfSize(size.x / 2d - 0.05d, 0.05d));
        }
    }

    public void step() {
        gravity = GameStates.gravity;
        val newPos = new Vector2d(position);
        val newVelocity = new Vector2d(velocity);
        if (gravity && !onGround) {
            newVelocity.add(new Vector2d(0, 0.005f));
            //newVelocity.y = Math.max(newVelocity.y, -0.001);
        }
        if (stage.getStageCollisionsForEntityWithFilter(this, new Stage.CollisionDownEntityPredicate(this)).size() > 0) {
            newVelocity.y = Math.min(0, newVelocity.y);
            onGround = true;
        } else onGround = false;
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_UP) && onGround)
            newVelocity.set(new Vector2d(0, -0.1f));
        if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            if (stage.getStageCollisionsForEntityWithFilter(this, new Stage.CollisionLeftEntityPredicate(this)).size() > 0)
                newVelocity.x = 0d;
            else
                newVelocity.x = !onGround ? -0.025 : -0.05f;
        } else if (InputHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            if (stage.getStageCollisionsForEntityWithFilter(this, new Stage.CollisionRightEntityPredicate(this)).size() > 0)
                newVelocity.x = 0d;
            else
                newVelocity.x = !onGround ? 0.025 : 0.05f;
        } else newVelocity.x = 0;
        velocity.set(newVelocity);
        newPos.add(velocity);
        position.set(newPos);

        if (GameStates.cameraPlayerLock)
            ClientGame.getInstance().getRunner().getUpdateLoop().getCameraPos().set(-position.x * 120 + 500, -position.y * 120 + 350);

        prevPosition.set(position);
        prevVelocity.set(velocity);

        wasOnGround = onGround;
        pushedRightWall = pushesRightWall;
        pushedLeftWall = pushesLeftWall;
        wasAtCeiling = atCeiling;
    }
}
