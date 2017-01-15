package com.p3k.magictale.engine.graphics.Objects;

/**
 * Created by COMar-PC on 15.01.2017.
 */
public class ObjectSheetResource {
    int id;
    int xInObjectSheet;
    int yInObjectSheet;
    int width;
    int height;

    public ObjectSheetResource(int id, int xInObjectSheet, int yInObjectSheet, int width, int height) {
        this.id = id;
        this.xInObjectSheet = xInObjectSheet;
        this.yInObjectSheet = yInObjectSheet;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public int getxInObjectSheet() {
        return xInObjectSheet;
    }

    public int getyInObjectSheet() {
        return yInObjectSheet;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
