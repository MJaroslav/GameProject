package com.github.mjaroslav.gameproject.client.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11.*;

@UtilityClass
public class GLStateManager {
    private final boolean[] states = new boolean[State.values().length];

    public void sync() {
        for (State state : State.values())
            states[state.ordinal()] = glIsEnabled(state.constant);
    }

    public boolean enable(int constant) {
        return enable(State.getFromConstant(constant));
    }

    public boolean enable(@NotNull State state) {
        if (!states[state.ordinal()]) {
            glEnable(state.constant);
            states[state.ordinal()] = true;
            return false;
        } else return true;
    }

    public boolean set(@NotNull State state, boolean value) {
        return value ? enable(state) : disable(state);
    }

    public boolean set(int constant, boolean value) {
        return value ? enable(constant) : disable(constant);
    }

    public boolean disable(int constant) {
        return disable(State.getFromConstant(constant));
    }

    public boolean disable(@NotNull State state) {
        if (states[state.ordinal()]) {
            glDisable(state.constant);
            states[state.ordinal()] = false;
            return true;
        } else return false;
    }

    public boolean isEnabled(int constant) {
        return isEnabled(State.getFromConstant(constant));
    }

    public boolean isEnabled(@NotNull State state) {
        return states[state.ordinal()];
    }

    @RequiredArgsConstructor
    public enum State {
        UNKNOWN(0),
        BLEND(GL_BLEND),
        CULL_FACE(GL_CULL_FACE),
        DEPTH_TEST(GL_DEPTH_TEST),
        DITHER(GL_DITHER),
        POLYGON_OFFSET_FILL(GL_POLYGON_OFFSET_FILL),
        // Where?
//        SAMPLE_ALPHA_TO_COVERAGE(GL_SAMPLE_ALPHA_TO_COVERAGE),
//        SAMPLE_COVERAGE(GL_SAMPLE_COVERAGE),
        SCISSOR_TEST(GL_SCISSOR_TEST),
        STENCIL_TEST(GL_STENCIL_TEST),
        TEXTURE_2D(GL_TEXTURE_2D);
        public final int constant;

        public static State getFromConstant(int constant) {
            for (State state : values())
                if (state.constant == constant)
                    return state;
            return UNKNOWN;
        }
    }
}
