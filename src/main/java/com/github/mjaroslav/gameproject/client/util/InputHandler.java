package com.github.mjaroslav.gameproject.client.util;

import com.github.mjaroslav.gameproject.common.util.GameStates;
import com.github.mjaroslav.gameproject.common.util.TimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InputHandler implements GLFWKeyCallbackI {
    private static InputHandler instance;

    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
            instance.init();
        }
        return instance;
    }

    private final boolean[] keyStates = new boolean[65536];
    private final double[] keyPressStartTime = new double[65536];
    private final List<KeyListener> keyListeners = new ArrayList<>();

    public static boolean isKeyPressed(int key) {
        return getInstance().keyStates[key];
    }

    public static boolean isKeyPressed(int... keys) {
        for (int key : keys)
            if (isKeyPressed(key))
                return true;
        return false;
    }

    public static boolean isShiftPressed() {
        return isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isAltPressed() {
        return isKeyPressed(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT);
    }

    public static boolean isCtrlPressed() {
        return isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static void registerKeyListener(int key, @NotNull KeyListener keyListener) {
        getInstance().keyListeners.add(keyListener);
    }

    public static List<KeyListener> getListeners(int key) {
        return getInstance().keyListeners.stream().filter(keyListener -> keyListener.forKey() == key)
                .collect(Collectors.toList());
    }

    private void init() {
        keyListeners.add(new KeyListenerAdapter(GLFW.GLFW_KEY_D) {
            @Override
            public void onKeyReleased() {
                GameStates.debugDrawing = !GameStates.debugDrawing;
            }
        });
        keyListeners.add(new KeyListenerAdapter(GLFW.GLFW_KEY_C) {
            @Override
            public void onKeyReleased() {
                GameStates.cameraPlayerLock = !GameStates.cameraPlayerLock;
            }
        });
        keyListeners.add(new KeyListenerAdapter(GLFW.GLFW_KEY_SPACE) {
            @Override
            public void onKeyReleased() {
                GameStates.gravity = !GameStates.gravity;
            }
        });
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW.GLFW_PRESS:
                keyStates[key] = true;
                keyPressStartTime[key] = TimeUtils.getTime();
                getListeners(key).forEach(KeyListener::onKeyPressed);
                break;
            case GLFW.GLFW_REPEAT:
                getListeners(key).forEach(listener ->
                        listener.onKeyRepeat(TimeUtils.getTime() - keyPressStartTime[key]));
                break;
            case GLFW.GLFW_RELEASE:
                keyStates[key] = false;
                keyPressStartTime[key] = 0;
                getListeners(key).forEach(KeyListener::onKeyReleased);
                break;
        }
    }
}
