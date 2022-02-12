package com.github.mjaroslav.gameproject.common.world;

import com.github.mjaroslav.gameproject.common.entity.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public abstract class World {
    @Getter
    protected final int width, height;
    protected final Stage[][] stages;
    @Getter
    protected Stage currentStage;
    @Getter
    @Setter
    protected Player player;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        stages = createStageMap(width, height);
    }

    @NotNull
    protected Stage[][] createStageMap(int width, int height) {
        return createEmptyStageMap(width, height);
    }

    public void setStage(@NotNull Stage stage, @NotNull Vector2i pos) {
        if (pos.x < 0 || pos.y < 0 || pos.x + stage.getScreensVertical() - 1 > width ||
                pos.y + stage.getScreensHorizontal() - 1 > height)
            throw new IllegalArgumentException("All stage screens should be in world!");
        for (var y = pos.y; y < pos.y + stage.screensVertical; y++)
            for (var x = pos.x; x < pos.x + stage.screensHorizontal; x++)
                stages[y][x] = stage;
    }

    @Nullable
    public Stage getStage(@NotNull Vector2i pos) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= width || pos.y >= height)
            return null;
        return stages[pos.y][pos.x];
    }

    public void step() {
        currentStage.step();
    }

    public void enterStage(@NotNull Vector2i pos) {
        val stage = getStage(pos);
        if (stage != null) {
            currentStage.onStageLeaved();
            currentStage = stage;
            currentStage.onStageEntered();
        }
    }

    public static Stage[][] createEmptyStageMap(int width, int height) {
        return new Stage[height][width];
    }
}
