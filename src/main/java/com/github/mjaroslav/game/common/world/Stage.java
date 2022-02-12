package com.github.mjaroslav.game.common.world;

import com.github.mjaroslav.game.common.block.Block;
import com.github.mjaroslav.game.common.block.Blocks;
import com.github.mjaroslav.game.common.entity.Entity;
import com.github.mjaroslav.game.common.util.AABBCollision;
import com.github.mjaroslav.game.common.util.CircleCollision;
import com.github.mjaroslav.game.common.util.CollisionManifold;
import com.github.mjaroslav.game.common.util.Collisions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Stage {
    public final int ONE_SCREEN_WIDTH = 20;
    public final int ONE_SCREEN_HEIGHT = 15;

    protected final World world;

    @Getter
    protected final int screensVertical, screensHorizontal;
    @Getter
    protected final int width, height;
    protected final Block[][] blocks;
    @Getter
    protected final List<Entity> entities = new ArrayList<>();
    @Getter
    protected final List<Object> compiledStageCollisions = new ArrayList<>();
    @Getter
    protected final List<CollisionManifold> manifolds = new ArrayList<>();

    public Stage(@NotNull World world, int screensVertical, int screensHorizontal) {
        this.world = world;
        this.screensVertical = screensVertical;
        this.screensHorizontal = screensHorizontal;
        width = screensHorizontal * ONE_SCREEN_WIDTH;
        height = screensVertical * ONE_SCREEN_HEIGHT;
        blocks = compileStage(width, height);
        compileStageCollisions();
    }

    @NotNull
    protected abstract Block[][] compileStage(int width, int height);

    @UnknownNullability
    public Block getBlock(@NotNull Vector2i pos) {
        if (pos.x < 0 || pos.y < 0 || pos.x > width || pos.y > height)
            return null;
        return blocks[pos.y][pos.x];
    }

    public List<Object> collectCollisionsFromAll() {
        val result = new ArrayList<>(compiledStageCollisions);
        entities.forEach(entity -> entity.takeCollisions(result));
        return result;
    }

    protected void compileStageCollisions() {
        compiledStageCollisions.clear();
        for (var y = 0; y < height; y++)
            for (var x = 0; x < width; x++)
                blocks[y][x].takeCollisions(compiledStageCollisions, this, new Vector2i(x, y));
    }

    @RequiredArgsConstructor
    public abstract static class EntityPredicate implements Predicate<Object> {
        @NotNull
        public final Entity entity;
    }

    public static abstract class CollisionEntityPredicate extends EntityPredicate {
        public final boolean defaultValue;
        public final int side;

        public CollisionEntityPredicate(@NotNull Entity entity, boolean defaultValue, int side) {
            super(entity);
            this.defaultValue = defaultValue;
            this.side = side;
        }

        @Override
        public boolean test(Object o) {
            if (o instanceof AABBCollision)
                return test((AABBCollision) o);
            if (o instanceof CircleCollision)
                return test((CircleCollision) o);
            return defaultValue;
        }

        public abstract boolean test(AABBCollision aabb);

        public abstract boolean test(CircleCollision circle);
    }

    public static final class CollisionDownEntityPredicate extends CollisionEntityPredicate {
        public CollisionDownEntityPredicate(@NotNull Entity entity) {
            super(entity, false, 3);
        }

        @Override
        public boolean test(AABBCollision aabb) {
            return aabb.getPosition().y >= entity.getPosition().y;
        }

        @Override
        public boolean test(CircleCollision circle) {
            return circle.getPosition().y >= entity.getPosition().y;
        }
    }

    public static final class CollisionUpEntityPredicate extends CollisionEntityPredicate {
        public CollisionUpEntityPredicate(@NotNull Entity entity) {
            super(entity, false, 2);
        }

        @Override
        public boolean test(AABBCollision aabb) {
            return aabb.getPosition().y <= entity.getPosition().y;
        }

        @Override
        public boolean test(CircleCollision circle) {
            return circle.getPosition().y <= entity.getPosition().y;
        }
    }

    public static final class CollisionRightEntityPredicate extends CollisionEntityPredicate {
        public CollisionRightEntityPredicate(@NotNull Entity entity) {
            super(entity, false, 1);
        }

        @Override
        public boolean test(AABBCollision aabb) {
            return aabb.getPosition().x >= entity.getPosition().x;
        }

        @Override
        public boolean test(CircleCollision circle) {
            return circle.getPosition().x >= entity.getPosition().x;
        }
    }

    public static final class CollisionLeftEntityPredicate extends CollisionEntityPredicate {
        public CollisionLeftEntityPredicate(@NotNull Entity entity) {
            super(entity, false, 0);
        }

        @Override
        public boolean test(AABBCollision aabb) {
            return aabb.getPosition().x <= entity.getPosition().x;
        }

        @Override
        public boolean test(CircleCollision circle) {
            return circle.getPosition().x <= entity.getPosition().x;
        }
    }

    public List<CollisionManifold> getStageCollisionsForEntityWithFilter(@NotNull Entity entity, @NotNull CollisionEntityPredicate filter) {
        val result = new ArrayList<CollisionManifold>();
        val entityCollisions = new ArrayList<>();
        entity.takeSensorCollisions(entityCollisions, filter.side);
        compiledStageCollisions.stream().filter(filter).forEach(stageCollision ->
                entityCollisions.forEach(entityCollision ->
                        result.add(Collisions.auto(entityCollision, stageCollision))));
        result.removeIf(manifold -> !manifold.isCollided());
        return result;
    }

    public List<CollisionManifold> getAllCollisions() {
        val result = new ArrayList<CollisionManifold>();
        entities.forEach(entity -> result.addAll(getAllCollisionsForEntity(entity)));
        return result;
    }

    public List<CollisionManifold> getAllCollisionsForEntity(@NotNull Entity entity) {
        val entityCollisions = new ArrayList<>();
        entity.takeCollisions(entityCollisions);
        val result = new ArrayList<CollisionManifold>();
        compiledStageCollisions.forEach(stageCollision ->
                entityCollisions.forEach(entityCollision ->
                        result.add(Collisions.auto(entityCollision, stageCollision))));
        val stageEntityCollisions = new ArrayList<>();
        entities.stream().filter(stageEntity -> stageEntity != entity).forEach(stageEntity -> {
            stageEntityCollisions.clear();
            stageEntity.takeCollisions(stageEntityCollisions);
            stageEntityCollisions.forEach(stageEntityCollision ->
                    entityCollisions.forEach(entityCollision ->
                            result.add(Collisions.auto(entityCollision, stageEntityCollision))));
        });
        result.removeIf(manifold -> !manifold.isCollided());
        return result;
    }

    public Entity spawnEntityInStage(@NotNull Entity entity) {
        entity.setStage(this);
        entities.add(entity);
        return entity;
    }

    public void step() {
        entities.forEach(Entity::step);
        entities.removeIf(Entity::isDead);
    }

    public void onStageEntered() {

    }

    public void onStageLeaved() {

    }

    public static Block[][] createEmptyBlockMap(int width, int height) {
        val result = new Block[height][width];
        for (var y = 0; y < height; y++)
            for (var x = 0; x < width; x++)
                result[y][x] = Blocks.air;
        return result;
    }
}
