package com.github.mjaroslav.gameproject.client.cycle;

import com.github.mjaroslav.gameproject.client.gui.screen.ScreenWorld;
import com.github.mjaroslav.gameproject.common.Constants;
import com.github.mjaroslav.gameproject.common.cycle.Timer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ClientRunner implements Runnable {
    @NotNull
    @Setter
    private ClientRenderLoop renderLoop;
    @NotNull
    @Setter
    private ClientUpdateLoop updateLoop;
    private final Timer renderTimer = new Timer(), updateTimer = new Timer();

    @Setter
    private boolean shouldStop;

    @Override
    public void run() {
        updateLoop.init();
        renderLoop.init();
        updateLoop.currentScreen = new ScreenWorld();
        double delta;
        double accumulator = 0d;
        double interval = 1d / Constants.GAME_UPDATE_RATE;
        double alpha;

        updateTimer.init();
        renderTimer.init();
        while (!shouldStop) {
            delta = updateTimer.getDelta();
            renderTimer.getDelta();
            accumulator += delta;

            while (accumulator >= interval) {
                updateLoop.beforeUpdate(1);
                updateLoop.update(1);
                updateLoop.afterUpdate(1);
                updateTimer.inc();
                accumulator -= interval;
            }

            alpha = accumulator / interval;

            renderLoop.beforeUpdate(alpha);
            renderLoop.update(alpha);
            renderLoop.afterUpdate(alpha);
            renderTimer.inc();

            updateTimer.update();
            renderTimer.update();

            //TimeUtils.sync(Constants.GAME_UPDATE_RATE, updateTimer);
        }
        renderLoop.destroy();
        updateLoop.destroy();
    }
}
