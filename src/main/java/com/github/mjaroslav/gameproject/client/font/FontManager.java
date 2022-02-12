package com.github.mjaroslav.gameproject.client.font;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FontManager {
    private static FontManager instance;

    public static FontManager getInstance() {
        if (instance == null)
            instance = new FontManager();
        return instance;
    }

    private Font defaultFont;

    public Font getDefaultFont() {
        if (defaultFont == null)
            defaultFont = new TrueTypeFont("JetBrainsMono-Regular.ttf", 24 * 8, 14 * 8);
        return defaultFont;
    }

    public void dispose() {
        getDefaultFont().dispose();
    }
}
