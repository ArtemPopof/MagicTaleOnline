package com.p3k.magictale.engine.graphics;

import java.io.Serializable;

/**
 * Created by artem96 on 03.12.16.
 */
public class Frame implements Serializable {

    private int length;
    private int numberDisplayed;
    private Sprite object;

    public Frame(Sprite sprite, int length) {
        this.object = sprite;
        this.length = length;

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


}
