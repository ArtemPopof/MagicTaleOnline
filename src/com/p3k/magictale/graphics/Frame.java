package com.p3k.magictale.graphics;

/**
 * Created by artem96 on 03.12.16.
 */
public class Frame {

    private int length;
    private int numberDisplayed;
    private Sprite object;

    public Frame(Sprite sprite, int length) {
        this.object = sprite;
        this.length = length;

        numberDisplayed = 0;
    }

    public boolean render() {

        object.render();
        numberDisplayed++;

        if (numberDisplayed >= length) {
            numberDisplayed = 0;
            return false;
        }

        return true;
    }


}
