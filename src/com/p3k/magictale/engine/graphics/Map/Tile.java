package com.p3k.magictale.engine.graphics.Map;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by COMar-PC on 07.12.2016.
 */
public class Tile extends GameObject {
    private TileProperties tileProperties = null;

    public Tile(Sprite sprite, float x, float y) {
        this.tileProperties = new TileProperties();
        super.initBySprite(sprite, x, y);
    }

    public Tile(Sprite sprite, float x, float y, TileProperties tileProperties) {
        this.tileProperties = tileProperties;
        super.initBySprite(sprite, x, y);
    }

    public Tile(int spriteId, float x, float y) {
        this.tileProperties = new TileProperties();
        super.initBySpriteId(spriteId, x, y);
    }

    public Tile(int spriteId, float x, float y, TileProperties tileProperties) {
        this.tileProperties = tileProperties;
        super.initBySpriteId(spriteId, x, y);
    }

    public TileProperties getTileProperties() {
        return tileProperties;
    }

//    public Sprite getSprite() { return this.getSprite(); }

    public void setTileProperties(TileProperties tileProperties) {
        this.tileProperties = tileProperties;
    }
}
