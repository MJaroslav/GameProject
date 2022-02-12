package com.github.mjaroslav.gameproject.common.util;

import com.github.mjaroslav.gameproject.common.cycle.Timer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {
    public double getTime() {
        return System.nanoTime() / 1_000_000_000d;
    }

    @SneakyThrows
    public void sleep(long mills) {
        Thread.sleep(mills);
    }

    public void sync(int updateRate, Timer timer) {
        double lastLoopTime = timer.getLastUpdateTime();
        double now = getTime();
        float targetTime = 1f / updateRate;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();
            sleep(1);
            now = getTime();
        }
    }
}
