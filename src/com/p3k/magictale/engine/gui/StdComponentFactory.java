package com.p3k.magictale.engine.gui;

/**
 * Created by jorgen on 14.12.16.
 */

import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.engine.graphics.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Creates standard look gui elements
 */
public class StdComponentFactory extends ComponentFactory {

    // Button images
    BufferedImage btnNormal, btnHovered, btnPressed;


    public StdComponentFactory() {
        RES_PATH = "res/gui/std";

        try {
            btnNormal = Utils.loadImage(RES_PATH + "/button_normal.bmp");
            btnHovered = Utils.loadImage(RES_PATH + "/button_hovered.bmp");
            btnPressed = Utils.loadImage(RES_PATH + "/button_pressed.bmp");
        } catch (IOException e) {
            System.err.println("Creating StdComponentFactory error: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public MButton createButton(String text, float x, float y) {
        StdButton button = new StdButton(text, x, y);

        int w = button.getWidth();
        int h = button.getHeight();

        button.setNormalSprite(new Sprite(btnNormal, w, h));
        button.setHoveredSprite(new Sprite(btnHovered, w, h));
        button.setPressedSprite(new Sprite(btnPressed, w, h));

        return button;
    }
}
