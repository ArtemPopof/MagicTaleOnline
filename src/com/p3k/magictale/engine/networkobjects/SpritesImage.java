package com.p3k.magictale.engine.networkobjects;

import java.net.URL;

/**
 * @author Александр Плосков
 *         Created on 06.12.2016.
 */
public class SpritesImage {
    private URL whereToDownload;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesCount;

    public SpritesImage(URL downloadUrl, int spriteWidth, int spriteHeight, int spritesCount) {
        whereToDownload = downloadUrl;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spritesCount = spritesCount;
    }

    public URL getURL() {
        return whereToDownload;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getSpritesCount() {
        return spritesCount;
    }
}
