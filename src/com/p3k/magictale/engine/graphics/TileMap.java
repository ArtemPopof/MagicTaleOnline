package com.p3k.magictale.engine.graphics;

import java.util.LinkedList;

/**
 * Created by COMar-PC on 14.12.2016.
 */
public class TileMap {
    // TODO Add positioning to TileMap from TileObjects
    private int xWidth = 0;
    private int yHeight = 0;
    private LinkedList<TileObject> tileObjects = null;

    public TileMap() {
        tileObjects = new LinkedList<>();
    }

    public TileMap(int yHeight, int xWidth) {
        this.yHeight = yHeight;
        this.xWidth = xWidth;
        tileObjects = new LinkedList<>();
    }

    public LinkedList<TileObject> getTileObjects() {
        return tileObjects;
    }

    public void setTileObjects(LinkedList<TileObject> tileObjects) {
        this.tileObjects = tileObjects;
    }
}
