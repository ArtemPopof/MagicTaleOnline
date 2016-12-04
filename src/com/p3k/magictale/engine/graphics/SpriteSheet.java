package com.p3k.magictale.engine.graphics;

import java.util.ArrayList;

/**
 * Created by artem96 on 04.12.16.
 */
public class SpriteSheet {

    ArrayList<Sprite> sprites;

    public SpriteSheet(String path) {

        loadSprites(path);

    }

    //TODO implement

    /**
     * Load spriteSheet and cut it to
     * individual sprites and put them to
     * sprites array
     *
     * @param path
     */
    private void loadSprites(String path) {

    }

    public int getSpriteTextureId(int index) {
        return sprites.get(index).getTextureId();
    }

    public int size() {
        return sprites.size();
    }

}
