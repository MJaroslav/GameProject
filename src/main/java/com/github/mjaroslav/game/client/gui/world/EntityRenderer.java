package com.github.mjaroslav.game.client.gui.world;

import com.github.mjaroslav.game.client.texture.Texture;
import com.github.mjaroslav.game.common.entity.Entity;
import com.github.mjaroslav.game.common.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class EntityRenderer {
    private static EntityRenderer instance;

    public static EntityRenderer getInstance() {
        if (instance == null) {
            instance = new EntityRenderer();
            instance.init();
        }
        return instance;
    }

    private final Map<Class<? extends Entity>, Texture> textures = new HashMap<>();

    public Texture getTextureForEntity(Entity entity) {
        return textures.get(entity.getClass());
    }

    private void init() {
        textures.put(Player.class, new Texture("test.png"));
    }
}
