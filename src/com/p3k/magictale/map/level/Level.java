package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.graphics.ResourceManager;

public interface Level {
    void load(String mapName, ResourceManager resourceManager);
    void render();
}
