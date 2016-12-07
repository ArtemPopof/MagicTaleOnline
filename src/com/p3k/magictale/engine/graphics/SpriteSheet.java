package com.p3k.magictale.engine.graphics;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by artem96 on 04.12.16.
 */
public class SpriteSheet {
    ArrayList<Sprite> sprites = null;

    public SpriteSheet(String path) {
        sprites = new ArrayList<>();

        try {
            loadSprites(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO implement

    /**
     * Load spriteSheet and cut it to
     * individual sprites and put them to
     * sprites array
     *
     * @param path
     */
    private void loadSprites(String path) throws IOException {
        System.out.println("HERE loadSprites");
        BufferedImage image = ImageIO.read(new File(path));
        System.out.println(image.getWidth() + " " + image.getHeight());

        int width = 32;
        int height = 32;
        int index = 0;
        for (int x = 0; x < 320; x += width) {
            for (int y = 0; y < 320; y += height, index++) {
//                System.out.println("x: " + x + " y: " + y + " w: " + width + " h: " + height);
                BufferedImage subImage = image.getSubimage(y, x, width, height);
                System.out.println("subImage x:" + subImage.getWidth() + " h: " + subImage.getHeight());
                Sprite sprite = null;
                try {
                    sprite = new Sprite(subImage, (float)width, (float)height);
                } catch (Exception e) {
                    System.out.println("Error create sprite from subImage" + e);
                }
                //ImageIO.write(subImage, "PNG", new File("res/map/background/tile" + index + ".png"));
                if (sprite != null) {
                    sprites.add(sprite);
                }
            }
            System.out.println("Sprites column count = " + sprites.size());
        }
        System.out.println("Sprites count = " + sprites.size());
    }

    public int getSpriteTextureId(int index) {
        return sprites.get(index).getTextureId();
    }

    public int size() {
        return sprites.size();
    }

}
