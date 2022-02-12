package com.github.mjaroslav.gameproject.client;

import com.github.mjaroslav.gameproject.client.cycle.ClientRenderLoop;
import com.github.mjaroslav.gameproject.client.cycle.ClientRunner;
import com.github.mjaroslav.gameproject.client.cycle.ClientUpdateLoop;
import com.github.mjaroslav.gameproject.client.font.FontManager;
import com.github.mjaroslav.gameproject.client.render.TextureManager;
import com.github.mjaroslav.gameproject.common.cycle.Timer;
import lombok.Getter;

public class ClientGame extends Thread {
    private static ClientGame instance;

    public static ClientGame getInstance() {
        if (instance == null)
            instance = new ClientGame();
        return instance;
    }

    public static ClientRenderLoop getRenderLoop() {
        return getInstance().renderLoop;
    }

    public static ClientUpdateLoop getUpdateLoop() {
        return getInstance().updateLoop;
    }

    public static Timer getRenderTimer() {
        return getInstance().runner.getRenderTimer();
    }

    public static Timer getUpdateTimer() {
        return getInstance().runner.getUpdateTimer();
    }

    public static int getFPS() {
        return getInstance().runner.getRenderTimer().getUpdateRate();
    }

    public static int getUPS() {
        return getInstance().runner.getUpdateTimer().getUpdateRate();
    }

    private final ClientUpdateLoop updateLoop;
    private final ClientRenderLoop renderLoop;

    @Getter
    private final ClientRunner runner;

    private ClientGame() {
        runner = new ClientRunner();
        updateLoop = new ClientUpdateLoop(runner);
        renderLoop = new ClientRenderLoop(runner, updateLoop);
        runner.setRenderLoop(renderLoop);
        runner.setUpdateLoop(updateLoop);
    }

    @Override
    public void run() {
        runner.run();
        TextureManager.getInstance().dispose();
        FontManager.getInstance().dispose();
    }
}
