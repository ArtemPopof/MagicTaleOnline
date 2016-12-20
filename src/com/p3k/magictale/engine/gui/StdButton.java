package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by jorgen on 14.12.16.
 */
public class StdButton extends MButton {

    private Sprite normalSprite, hoverSprite, pressedSprite;

    /**
     * Need by factory constructor
     * @param text
     * @param x
     * @param y
     */
    public StdButton(MText text, float x, float y) {
        super(text, x, y);
    }

    // EVENTS


}
