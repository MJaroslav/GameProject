package com.github.mjaroslav.game.common.cycle;

import com.github.mjaroslav.game.common.util.TimeUtils;
import lombok.val;

public class Timer {
    private double lastUpdateTime;
    private float timeCounter;
    private int updateRate;
    private int updates;

    public void init() {
        lastUpdateTime = TimeUtils.getTime();
    }

    public double getDelta() {
        val time = TimeUtils.getTime();
        val delta = time - lastUpdateTime;
        lastUpdateTime = time;
        timeCounter += delta;
        return delta;
    }

    public void inc() {
        updates++;
    }

    public void update() {
        if (timeCounter > 1d) {
            updateRate = updates;
            updates = 0;
            timeCounter -= 1d;
        }
    }

    public int getUpdateRate() {
        return updateRate > 0 ? updateRate : updates;
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }
}
