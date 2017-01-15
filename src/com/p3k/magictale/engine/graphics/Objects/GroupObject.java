package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.GameObject;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class GroupObject extends GameObject {
    private int xTileSheet = 0;
    private int yTileSheet = 0;
    private int widthNum = 1;
    private int heightNum = 1;

    private String type = null;
    private String name = null;

    private GroupObjectProperties groupObjectProperties = null;

    public GroupObject(int xTileSheet, int yTileSheet) {
        this.xTileSheet = xTileSheet;
        this.yTileSheet = yTileSheet;
    }

    public GroupObject(int xTileSheet, int yTileSheet, int widthNum, int heightNum, String type, String name, int id) {
        this.xTileSheet = xTileSheet;
        this.yTileSheet = yTileSheet;
        this.widthNum = widthNum;
        this.heightNum = heightNum;
        this.type = type;
        this.name = name;
        this.groupObjectProperties = new GroupObjectProperties();
        this.spriteId = id;
    }

    public GroupObject(int x, int y, String type, String name, GroupObjectProperties groupObjectProperties) {
        this.x = (float)x;
        this.y = (float)y;
        this.type = type;
        this.name = name;
        this.groupObjectProperties = groupObjectProperties;
    }

    public int getxTileSheet() {
        return xTileSheet;
    }

    public void setxTileSheet(int xTileSheet) {
        this.xTileSheet = xTileSheet;
    }

    public int getyTileSheet() {
        return yTileSheet;
    }

    public void setyTileSheet(int yTileSheet) {
        this.yTileSheet = yTileSheet;
    }

    public int getWidthNum() {
        return widthNum;
    }

    public void setWidthNum(int widthNum) {
        this.widthNum = widthNum;
    }

    public int getHeightNum() {
        return heightNum;
    }

    public void setHeightNum(int heightNum) {
        this.heightNum = heightNum;
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

    public GroupObjectProperties getGroupObjectProperties() {
        return groupObjectProperties;
    }

    public void setGroupObjectProperties(GroupObjectProperties groupObjectProperties) {
        this.groupObjectProperties = groupObjectProperties;
    }
}
