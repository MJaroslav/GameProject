package com.github.mjaroslav.gameproject.common.world.impl;

import com.github.mjaroslav.gameproject.common.entity.Player;
import com.github.mjaroslav.gameproject.common.world.World;
import org.joml.Vector2d;
import org.joml.Vector2i;

public class BaseWorld extends World {
    public BaseWorld() {
        super(1, 1);
        setStage(new BaseStage(this), new Vector2i(0, 0));
        currentStage = getStage(new Vector2i(0, 0));
        setPlayer((Player) currentStage.spawnEntityInStage(new Player(new Vector2d(4, 4), new Vector2d(1, 1))));
    }
}
