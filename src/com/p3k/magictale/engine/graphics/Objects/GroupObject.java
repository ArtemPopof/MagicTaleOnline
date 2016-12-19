package com.p3k.magictale.engine.graphics.Objects;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class GroupObject{
    int x = 0;
    int y = 0;
    int widthNum = 1;
    int heightNum = 1;
    private GroupObjectProperties groupObjectProperties = null;

    public GroupObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GroupObject(int x, int y, GroupObjectProperties groupObjectProperties) {
        this.x = x;
        this.y = y;
        this.groupObjectProperties = groupObjectProperties;
    }
}
