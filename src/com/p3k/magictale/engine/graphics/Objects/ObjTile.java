package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjTile extends GameObject {
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
