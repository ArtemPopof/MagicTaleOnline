package com.p3k.magictale.map.level;

import client.ClientMessage;
import com.p3k.magictale.engine.graphics.ResourceManager;

import java.util.LinkedList;

public interface Level {
    void loadClient(String mapName, ResourceManager resourceManager);
    void loadServer(String mapName, LinkedList<ClientMessage> messagesToClient);
    void render();
}
