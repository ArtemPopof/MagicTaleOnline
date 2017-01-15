package com.p3k.magictale.engine.graphics;

import java.io.Serializable;

/**
 * Created by artem96 on 03.12.16.
 */
public class Frame implements Serializable {

    private int length;
    private int numberDisplayed;
    private Sprite object;

    /**
     * Sprite id in ResourceManager
     */
    private int spriteId;

    public Frame(Sprite sprite, int spriteId, int length) {
        this.object = sprite;
        this.length = length;
        this.spriteId = spriteId;

        numberDisplayed = 0;
    }

    public boolean update() {

        numberDisplayed++;

        if (numberDisplayed >= length) {
            numberDisplayed = 0;
            return false;
        }

        return true;

    }

    public Sprite getSprite() {
        return object;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setFrameSize(float width, float height) {
        this.object.setWidth(width);
        this.object.setHeight(height);
    }

}
