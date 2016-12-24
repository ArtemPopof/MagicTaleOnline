package com.p3k.magictale.engine.graphics.Map;

import com.p3k.magictale.engine.graphics.ITileProperties;

/**
 * Created by COMar-PC on 13.12.2016.
 */
public class TileProperties implements ITileProperties{
    private boolean isPass = false;
    private boolean isFly = false;
    private int layer = 0;

    public TileProperties(){
    }

    public TileProperties(int layer, boolean isPass) {
        this.isPass = isPass;
        this.layer = layer;
    }

    public TileProperties(int layer, boolean isPass, boolean isFly) {
        this.isPass = isPass;
        this.isFly = isFly;
        this.layer = layer;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public boolean isFly() {
        return isFly;
    }

    public void setFly(boolean fly) {
        isFly = fly;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
