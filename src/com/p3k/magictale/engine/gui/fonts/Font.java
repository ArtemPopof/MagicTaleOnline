package com.p3k.magictale.engine.gui.fonts;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.SpriteSheet;

import java.awt.*;
import java.io.*;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorgen on 16.12.16.
 */
public class Font {

    private static final int CHAR_SPRITE_WIDTH = 32;
    private static final int CHAR_SPRITE_HEIGHT = 32;

    private static final String FONT_DIR = "res/gui/fonts/";

    private SpriteSheet sheet;

    private String name;

    private int size;
    private float space;
    private Color color;

    public Font(String name) throws Exception {
        this.name = name;

        this.loadFont();

        // Default font values
        setSize(32);
        space = -20.0f;
    }

    public Sprite getSpriteFor(char ch) throws NoSuchObjectException {
        try {
            int pos = ch;
            return sheet.getSprites().get(ch);
        } catch (Exception e) {
            throw new NoSuchObjectException("No sprite for symbol " + ch + " is provided!");
        }
    }

    private void loadFont() throws Exception {
        sheet = new SpriteSheet(FONT_DIR + name + ".bmp",
                CHAR_SPRITE_WIDTH, CHAR_SPRITE_HEIGHT, false, true);

        if ( sheet == null ) {
            throw new FileNotFoundException("Can't find file with name: "
                    + name + ".png");
        }

    }

    private void resize() {
        sheet.getSprites().forEach(sprite -> {
            sprite.setHeight(size);
            sprite.setWidth(size);
        });
    }

    public float getSpace() {
        return space;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {

        this.size = size;
        this.resize();
    }
}
