package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.graphics.ResourceManager;
import server.ServerObject;

import java.util.Map;

public interface Level {
    void loadClient(String mapName, ResourceManager resourceManager);
    void loadServer(String mapName);
    void render();
}
