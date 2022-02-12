package com.github.mjaroslav.gameproject.client.cycle;

import com.github.mjaroslav.gameproject.client.gui.Screen;
import com.github.mjaroslav.gameproject.client.util.InputHandler;
import com.github.mjaroslav.gameproject.common.cycle.Loop;
import com.github.mjaroslav.gameproject.common.entity.Player;
import com.github.mjaroslav.gameproject.common.world.World;
import com.github.mjaroslav.gameproject.common.world.impl.BaseWorld;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

import static org.lwjgl.glfw.GLFW.*;

@RequiredArgsConstructor
public final class ClientUpdateLoop implements Loop {
    @NotNull
    final ClientRunner runner;
    @Getter
    final Vector2d cameraPos = new Vector2d();
    @Getter
    @Setter
    Screen currentScreen;

    @Getter
    @Setter
    World world;
    @Getter
    Player player;

    @Override
    public void init() {
        world = new BaseWorld();
    }

    boolean lockCameraOnPlayer;

    @Override
    public void beforeUpdate(double delta) {
        if (InputHandler.isKeyPressed(GLFW_KEY_KP_8)) // up
            cameraPos.add(0, 60);
        if (InputHandler.isKeyPressed(GLFW_KEY_KP_2)) // down
            cameraPos.add(0, -60);
        if (InputHandler.isKeyPressed(GLFW_KEY_KP_4)) // left
            cameraPos.add(60, 0);
        if (InputHandler.isKeyPressed(GLFW_KEY_KP_6)) // right
            cameraPos.add(-60, 0);
    }

    @Override
    public void update(double delta) {
        if (world != null)
            world.step();
    }
}
