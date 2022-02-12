package com.github.mjaroslav.gameproject.common.world.impl;

import com.github.mjaroslav.gameproject.common.block.Block;
import com.github.mjaroslav.gameproject.common.block.Blocks;
import com.github.mjaroslav.gameproject.common.world.Stage;
import com.github.mjaroslav.gameproject.common.world.World;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;

public class BaseStage extends Stage {
    public BaseStage(@NotNull World world) {
        super(world, 1, 1);
    }

    @Override
    protected @NotNull Block[][] compileStage(int width, int height) {
        val result = Stage.createEmptyBlockMap(width, height);
        for (var y = 0; y < height; y++)
            for (var x = 0; x < width; x++)
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1)
                    result[y][x] = Blocks.wall;
        result[height-2][4] = Blocks.wall;
        result[height-3][5] = Blocks.wall;
        result[height-4][6] = Blocks.wall;
        result[height-5][7] = Blocks.wall;
        result[height-6][8] = Blocks.test;
        return result;
    }
}
