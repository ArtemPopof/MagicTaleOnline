package com.p3k.magictale.engine.gui;

/**
 * Created by jorgen on 14.12.16.
 */

import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.gui.fonts.Font;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Creates standard look gui elements
 */
public class StdComponentFactory extends ComponentFactory {

    // Button images
    BufferedImage btnNormal, btnHovered, btnPressed;

    // Std font
    Font defaultFont;

    public StdComponentFactory() {
        RES_PATH = "res/gui/std";

        try {
            btnNormal = Utils.loadImage(RES_PATH + "/button_normal.bmp");
            btnHovered = Utils.loadImage(RES_PATH + "/button_hovered.bmp");
            btnPressed = Utils.loadImage(RES_PATH + "/button_pressed.bmp");

            defaultFont = new Font("default");
        } catch (IOException e) {
            System.err.println("Creating StdComponentFactory error: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Creating StdComponentFactory error: " + e);
            e.printStackTrace();
        }
    }

    public MText createText(String text) {
        return new MText(text, defaultFont);
    }

    @Override
    public MButton createButton(String text, float x, float y) {
        StdButton button = new StdButton(createText(text), x, y);

        int w = button.getWidth();
        int h = button.getHeight();

        button.setNormalSprite(new Sprite(btnNormal, btnNormal.getWidth(), btnNormal.getHeight()));
        button.setHoveredSprite(new Sprite(btnHovered, btnHovered.getWidth(), btnHovered.getHeight()));
        button.setPressedSprite(new Sprite(btnPressed, btnPressed.getWidth(), btnPressed.getHeight()));

        // Need to adjust button and text sizes
        button.update();

        return button;
    }
}
