package com.p3k.magictale.engine.gui.fonts;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.SpriteSheet;

import java.awt.*;
import java.rmi.NoSuchObjectException;

/**
 * Created by jorgen on 16.12.16.
 */
public class Font {
    private SpriteSheet sheet;

    private String name;

    private FontInfo info;

    public Font(String name, FontInfo info, SpriteSheet sheet) {
        this.name = name;
        this.info = info;
        this.sheet = sheet;

        this.setSize(info.size);
    }

    public Font(Font another) {
        this.sheet = another.sheet;
        this.name = another.name;
        this.info = another.info;
    }

    public Sprite getSpriteFor(char ch) throws NoSuchObjectException {
        try {
            int pos = ch;
            return sheet.getSprites().get(ch);
        } catch (Exception e) {
            throw new NoSuchObjectException("No sprite for symbol " + ch + " is provided!");
        }
    }

    private void resize() {
        sheet.getSprites().forEach(sprite -> {
            sprite.setHeight(info.size);
            sprite.setWidth(info.size);
        });
    }

    public float getSpace() {
        return info.space;
    }

    public void setSpace(int space) {
        info.space = space;
    }

    public Color getColor() {
        return null;
    }

    public void setColor(Color color) {
        return;
    }

    public int getSize() {
        return info.size;
    }

    public void setSize(int size) {

        if ( info.size == size ) {
            return;
        }

        float dsize = size / (float) info.size;

        info.size = size;
        info.space = (int) (info.space * dsize);

        this.resize();
    }
}
