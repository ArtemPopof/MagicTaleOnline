package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.networkobjects.SpritesImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author artem96
 * @author Александр Плосков
 *         Created on 04.12.16.
 */
public class SpriteSheet {
    ArrayList<Sprite> sprites = null;

    /**
     * Load spriteSheet and cut it to
     * individual sprites and put them to
     * sprites array
     *
     * @param image - BufferedImage, which contains some sprites, include fully transparent unnecessary sprites
     */
    public SpriteSheet(SpritesImage image) {
        sprites = new ArrayList<>();

        try {
            loadSprites(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO implement

    private void loadSprites(SpritesImage image) throws IOException {
        BufferedImage img = ImageIO.read(image.getURL());

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        int spriteWidth = image.getSpriteWidth();
        int spriteHeight = image.getSpriteHeight();

        for (int y = 0; y + spriteHeight <= imgHeight; y += spriteHeight) {
            for (int x = 0; x + spriteWidth <= imgWidth; x += spriteWidth) {
                BufferedImage subImage = img.getSubimage(x, y, spriteWidth, spriteHeight);
                Sprite sprite = new Sprite(subImage);
                sprites.add(sprite);
            }
        }
    }

    public int getSpriteTextureId(int index) {
        return sprites.get(index).getTextureId();
    }

    public int size() {
        return sprites.size();
    }

}
