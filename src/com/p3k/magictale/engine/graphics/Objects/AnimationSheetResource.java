package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.SpriteSheet;

import java.util.ArrayList;

/**
 * Contain information about mapping of
 * particular spriteSheet in ResourceManager
 *
 * Created by artem96 on 14.01.17.
 */
public class AnimationSheetResource {

    /**
     * First id in ResourceManager of this sprite sheet
     */
    private int startOffset;

    /**
     * Length of spriteSheet
     */
    private int length;

    private ArrayList<Sprite> sprites;

    public AnimationSheetResource(SpriteSheet ss, int firstId) {

        this.startOffset = firstId;
        this.length = ss.getSprites().size();
        this.sprites = ss.getSprites();
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    /**
     * return sprite id in ResourceManager
     *
     * @param offsetInSpriteSheet
     * @return
     */
    public int getSpriteId(int offsetInSpriteSheet) {
        return startOffset + offsetInSpriteSheet;
    }


}
