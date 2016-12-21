package com.p3k.magictale.engine.graphics.Objects;

import java.io.Serializable;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjTileProperties implements Serializable {
    private boolean isPass = false;
    private boolean isFly = false;

    public ObjTileProperties(){
    }

    public ObjTileProperties(int layer, boolean isPass) {
        this.isPass = isPass;
    }

    public ObjTileProperties(int layer, boolean isPass, boolean isFly) {
        this.isPass = isPass;
        this.isFly = isFly;
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
}
