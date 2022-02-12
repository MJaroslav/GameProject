package com.github.mjaroslav.game.client.cycle;

import com.github.mjaroslav.game.client.ClientGame;
import com.github.mjaroslav.game.client.util.GLStateManager;
import com.github.mjaroslav.game.common.cycle.Loop;
import com.github.mjaroslav.game.client.util.InputHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@RequiredArgsConstructor
public final class ClientRenderLoop implements Loop {
    @NotNull
    private final ClientRunner runner;
    @NotNull
    private final ClientUpdateLoop updateLoop;
    @Getter
    private long windowId;


    @Override
    public void init() {
        System.out.println("LWJGL " + Version.getVersion());
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        // TODO: OGL 3
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        windowId = glfwCreateWindow(1000, 700, "Game", NULL, NULL);

        glfwSetKeyCallback(windowId, InputHandler.getInstance());

        glfwSetWindowCloseCallback(windowId, window1 -> runner.setShouldStop(true));

        try (val stack = stackPush()) {
            val pWidth = stack.mallocInt(1);
            val pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowId, pWidth, pHeight);

            val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (videoMode != null)
                glfwSetWindowPos(
                        windowId,
                        (videoMode.width() - pWidth.get(0)) / 2,
                        (videoMode.height() - pHeight.get(0)) / 2
                );
        }

        glfwMakeContextCurrent(windowId);

        glfwSwapInterval(0); // 1 for V-sync

        glfwShowWindow(windowId);

        GL.createCapabilities();

        GLStateManager.sync();

        glClearColor(1f, 1f, 1f, 0f);
        updateViewPort();
    }

    public void updateViewPort() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glViewport(0, 0, 1000, 700);
        glOrtho(0, 1000, 700, 0, 0, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }


    @Override
    public void update(double delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glPushMatrix();
        glTranslated(updateLoop.cameraPos.x, updateLoop.cameraPos.y, 0);

        render(delta);

        glPopMatrix();

        glfwSwapBuffers(windowId);
        glfwPollEvents();
    }

    @Override
    public void destroy() {
        glfwFreeCallbacks(windowId);
        glfwDestroyWindow(windowId);

        glfwTerminate();
        val prevCallback = glfwSetErrorCallback(null);
        if (prevCallback != null) prevCallback.free();
    }

    private void render(double delta) {
        glfwSetWindowTitle(windowId, String.format("FPS: %s, TPS: %s, CAM: %Sx%S, PARTIAL: %s", ClientGame.getFPS(),
                ClientGame.getUPS(), updateLoop.cameraPos.x, updateLoop.cameraPos.y, delta));

        if (updateLoop.currentScreen != null)
            updateLoop.currentScreen.draw(delta);
    }
}
