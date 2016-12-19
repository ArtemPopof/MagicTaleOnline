package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.ResourceManager;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public interface ObjectInterface {
    void load(String mapName, ResourceManager resourceManager);
    void render();
}
