package com.github.mjaroslav.game.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {
    public double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}
