package com.p3k.magictale.engine.graphics;

/**
 * Created by COMar-PC on 07.12.2016.
 */
public class TileObject extends GameObject {
    private TileProperties tileProperties = null;

    public TileObject(Sprite sprite, float x, float y) {
        this.tileProperties = new TileProperties();
        super.initBySprite(sprite, x, y);
    }

    public TileObject(Sprite sprite, float x, float y, TileProperties tileProperties) {
        this.tileProperties = tileProperties;
        super.initBySprite(sprite, x, y);
    }
}
