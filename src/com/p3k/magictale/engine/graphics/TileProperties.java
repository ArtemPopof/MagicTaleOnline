package com.p3k.magictale.engine.graphics;

/**
 * Created by COMar-PC on 13.12.2016.
 */
public class TileProperties {
    private boolean isPass = false;
    private int layer = 0;

    public TileProperties(){
    }

    public TileProperties(boolean isPass, int layer) {
        this.isPass = isPass;
        this.layer = layer;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
