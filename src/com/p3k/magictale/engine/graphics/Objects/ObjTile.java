package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Sprite;

import java.io.Serializable;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjTile extends GameObject implements Serializable {
    private ObjTileProperties objTileProperties = null;
    private String type = null;
    private String name = null;
    private int idInTypeName = 0;

    public ObjTile(Sprite sprite, float x, float y) {
        this.objTileProperties = new ObjTileProperties();
        super.initBySprite(sprite, x, y);
    }

    public ObjTile(Sprite sprite, float x, float y, ObjTileProperties objTileProperties) {
        this.objTileProperties = objTileProperties;
        super.initBySprite(sprite, x, y);
    }

    public ObjTile(ObjTile objTile) {
        this.objTileProperties = objTile.getObjTileProperties();
        this.type = objTile.getType();
        this.name = objTile.getName();
        this.idInTypeName = objTile.getIdInTypeName();
        super.initBySprite(objTile.getSprite(), objTile.getX(), objTile.getY());
    }

    public ObjTile(Tile tile, int x, int y) {
        this.objTileProperties = new ObjTileProperties();
        this.objTileProperties.setPass(tile.getTileProperties().isPass());
        this.objTileProperties.setFly(tile.getTileProperties().isFly());
        super.initBySprite(tile.getSprite(), x, y);
    }

    public ObjTileProperties getObjTileProperties() {
        return objTileProperties;
    }

    public void setObjTileProperties(ObjTileProperties objTileProperties) {
        this.objTileProperties = objTileProperties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdInTypeName() {
        return idInTypeName;
    }

    public void setIdInTypeName(int idInTypeName) {
        this.idInTypeName = idInTypeName;
    }
}
