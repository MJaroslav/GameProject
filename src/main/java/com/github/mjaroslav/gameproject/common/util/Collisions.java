package com.github.mjaroslav.gameproject.common.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

@UtilityClass
public class Collisions {
    public CollisionManifold auto(@NotNull Object first, @NotNull Object second) {
        val firstIsAABB = first instanceof AABBCollision;
        val firstIsCircle = !firstIsAABB && first instanceof CircleCollision;
        val secondIsAABB = second instanceof AABBCollision;
        val secondIsCircle = !secondIsAABB && second instanceof CircleCollision;
        if (firstIsCircle && secondIsCircle)
            return circleVsCircle((CircleCollision) first, (CircleCollision) second);
        if (firstIsAABB && secondIsAABB)
            return AABBVsAABB((AABBCollision) first, (AABBCollision) second);
        if (firstIsAABB && secondIsCircle)
            return AABBVsCircle((AABBCollision) first, (CircleCollision) second);
        if (firstIsCircle && secondIsAABB)
            return AABBVsCircle((AABBCollision) second, (CircleCollision) first);
        return new CollisionManifold(first, second);
    }

    public CollisionManifold circleVsCircle(@NotNull CircleCollision first, @NotNull CircleCollision second) {
        val result = new CollisionManifold(first, second);
        val n = new Vector2d(second.position).sub(first.position);
        var r = first.radius + second.radius;
        r *= r;
        result.collided = n.lengthSquared() <= r;
        if (!result.collided)
            return result;
        double d = n.length();
        if (d != 0) {
            result.penetration = r - d;
            result.normal.set(n).div(d).normalize();
        } else {
            result.penetration = first.radius;
            result.normal.set(1d, 0d);
        }
        return result;
    }

    public CollisionManifold AABBVsAABB(@NotNull AABBCollision first, @NotNull AABBCollision second) {
        val result = new CollisionManifold(first, second);
        val n = new Vector2d(second.position).sub(first.position);
        var firstExtent = first.halfSize.x;
        var secondExtent = second.halfSize.x;
        val xOverlap = firstExtent + secondExtent - Math.abs(n.x);
        if (xOverlap > 0) {
            firstExtent = first.halfSize.y;
            secondExtent = second.halfSize.y;
            val yOverlap = firstExtent + secondExtent - Math.abs(n.y);
            if (yOverlap > 0) {
                if (xOverlap > yOverlap) {
                    if (n.x < 0)
                        result.normal.set(-1d, 0d);
                    else
                        result.normal.set(1d, 0d);
                    result.penetration = xOverlap;

                } else {
                    if (n.y < 0)
                        result.normal.set(0d, -1d);
                    else
                        result.normal.set(0d, 1d);
                    result.penetration = yOverlap;
                }
                result.collided = true;
                return result;
            }
        }
        result.collided = false;
        return result;
    }

    public CollisionManifold AABBVsCircle(@NotNull AABBCollision first, @NotNull CircleCollision second) {
        val result = new CollisionManifold(first, second);
        val n = new Vector2d(second.position).sub(first.position);
        val closest = new Vector2d(n);
        val xExtent = first.halfSize.x;
        val yExtent = first.halfSize.y;
        closest.x = MathUtils.clamp(closest.x, -xExtent, xExtent);
        closest.y = MathUtils.clamp(closest.y, -yExtent, yExtent);
        var inside = false;
        if (n.equals(closest)) {
            inside = true;
            if (Math.abs(n.x) > Math.abs(n.y)) {
                if (closest.x > 0)
                    closest.x = xExtent;
                else
                    closest.x = -xExtent;
            } else {
                if (closest.y > 0)
                    closest.y = yExtent;
                else
                    closest.y = -yExtent;
            }
        }
        val normal = new Vector2d(n).sub(closest);
        var d = normal.lengthSquared();
        val r = second.radius;
        if (d > r * r && !inside) {
            result.collided = false;
            return result;
        }
        d = Math.sqrt(d);
        if (inside)
            result.normal.set(n).negate();
        else
            result.normal.set(n);
        result.normal.normalize();
        result.penetration = r - d;
        result.collided = true;
        return result;
    }
}
