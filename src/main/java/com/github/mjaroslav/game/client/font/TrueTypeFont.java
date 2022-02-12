package com.github.mjaroslav.game.client.font;

import com.github.mjaroslav.game.client.util.GLStateManager;
import com.github.mjaroslav.game.client.util.IOUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TrueTypeFont implements Font {
    public static final int MAX_TABLES = 1024;

    private final float[] scale;
    private final int bitmapW;
    private final int bitmapH;

    private final String resource;

    private final int[] tables = new int[MAX_TABLES];
    private final boolean[] loadedTables = new boolean[MAX_TABLES];
    private final STBTTPackedchar.Buffer charData;

    @Getter
    @Setter
    private boolean integerAlign;
    @Getter
    @Setter
    private int font;

    private final STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
    private final FloatBuffer xBuffer = memAllocFloat(1);
    private final FloatBuffer yBuffer = memAllocFloat(1);

    public TrueTypeFont(@NotNull String resource, float scaleX, float scaleY) {
        this.resource = resource;
        scale = new float[]{scaleX, scaleY};
        var scale = Math.max(scaleX / 24d, scaleY / 14d);
        scale = Math.ceil(Math.log(scale) / Math.log(2));
        scale = Math.max(512 * scale, 512);
        bitmapW = (int) scale;
        bitmapH = (int) scale;
        charData = STBTTPackedchar.malloc(6 * 128 * MAX_TABLES);
        loadTable(0);
    }

    @Override
    public double getHeight() {
        return scale[0];
    }

    @Override
    public void drawLine(double x, double y, @NotNull String line) {
        var STATE_CULL_FACE = GLStateManager.disable(GL_CULL_FACE);
        var STATE_TEXTURE_2D = GLStateManager.disable(GL_TEXTURE_2D);
        var STATE_LIGHTING = GLStateManager.disable(GL_LIGHTING);
        var STATE_DEPTH_TEST = GLStateManager.disable(GL_DEPTH_TEST);
        double offsetY = y;
        for (var splitLine : line.split("\n")) {
            xBuffer.put(0, (float) x);
            yBuffer.put(0, (float) offsetY);

            GLStateManager.enable(GL_TEXTURE_2D);


            var currTable = -1;
            var drawing = false;
            for (var i = 0; i < splitLine.length(); i++) {
                var charTable = splitLine.charAt(i) / 128;
                if (charTable != currTable) {
                    if (drawing) glEnd();
                    if (!loadedTables[charTable])
                        loadTable(charTable);
                    glBindTexture(GL_TEXTURE_2D, tables[charTable]);
                    currTable = charTable;
                    glBegin(GL_QUADS);
                    drawing = true;
                }
                stbtt_GetPackedQuad(charData, bitmapW, bitmapH, splitLine.charAt(i), xBuffer, yBuffer, quad, font == 0 && integerAlign);
                drawChar(quad.x0(), quad.y0(), quad.x1(), quad.y1(), quad.s0(), quad.t0(), quad.s1(), quad.t1()
                );
            }
            if (drawing)
                glEnd();
            offsetY += getHeight();
        }
        GLStateManager.set(GL_CULL_FACE, STATE_CULL_FACE);
        GLStateManager.set(GL_TEXTURE_2D, STATE_TEXTURE_2D);
        GLStateManager.set(GL_LIGHTING, STATE_LIGHTING);
        GLStateManager.set(GL_DEPTH_TEST, STATE_DEPTH_TEST);
    }

    @Override
    public double getLineWidth(@NotNull String line) {
        var result = 0d;
        for (var i = 0; i < line.length(); i++) {
            stbtt_GetPackedQuad(charData, bitmapW, bitmapH, line.charAt(i), xBuffer, yBuffer, quad, font == 0 && integerAlign);
            result += quad.x0() + quad.x1();
        }
        return result;
    }


    @Override
    public void dispose() {
        charData.free();
        for (var i = 0; i < MAX_TABLES; i++) {
            if (loadedTables[i]) {
                glDeleteTextures(tables[i]);
                loadedTables[i] = false;
                tables[i] = 0;
            }
        }
        memFree(yBuffer);
        memFree(xBuffer);
        quad.free();
    }

    private void loadTable(int table) {
        tables[table] = glGenTextures();
        try (val pc = STBTTPackContext.malloc()) {
            val ttf = IOUtil.ioResourceToByteBuffer(resource, 512 * 1024);
            val bitmap = BufferUtils.createByteBuffer(bitmapW * bitmapH);
            stbtt_PackBegin(pc, bitmap, bitmapW, bitmapH, 0, 1, NULL);
            for (var i = 0; i < 2; i++) {
                var p = (i * 3) * 128 + 128 * table;
                charData.limit(p + 128);
                charData.position(p);
                stbtt_PackSetOversampling(pc, 1, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 128 * table, charData);

                p = (i * 3 + 1) * 128 + 128 * table;
                charData.limit(p + 128);
                charData.position(p);
                stbtt_PackSetOversampling(pc, 2, 2);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 128 * table, charData);

                p = (i * 3 + 2) * 128 + 128 * table;
                charData.limit(p + 128);
                charData.position(p);
                stbtt_PackSetOversampling(pc, 3, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 128 * table, charData);
            }

            stbtt_PackEnd(pc);
            //stbi_write_bmp(String.format("font_texture%s.bmp", table), bitmapW, bitmapH, 1, bitmap);
            glBindTexture(GL_TEXTURE_2D, tables[table]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, bitmapW, bitmapH, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedTables[table] = true;
        charData.clear();
    }

    private static void drawChar(float x0, float y0, float x1, float y1, float s0, float t0, float s1, float t1) {
        glTexCoord2f(s0, t0);
        glVertex2f(x0, y0);
        glTexCoord2f(s1, t0);
        glVertex2f(x1, y0);
        glTexCoord2f(s1, t1);
        glVertex2f(x1, y1);
        glTexCoord2f(s0, t1);
        glVertex2f(x0, y1);
    }
}
