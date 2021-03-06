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

    protected int tileWidth;
    protected int tileHeight;

    private BufferedImage image;

    public SpriteSheet() {
        sprites = new ArrayList<>();
    }

    public SpriteSheet(String path) {
        sprites = new ArrayList<>();

        try {
            loadSprites(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpriteSheet(String path, int width, int height) {
        this(path, width, height, true, false);
    }
    /**
     *
     * Creates spritesheet representation of png image,
     * containing sprites. Use getSprites to obtain sprites)
     *
     * @param path path to the spritesheet image
     * @param width width of single tile
     * @param height height of single tile
     */
    public SpriteSheet(String path, int width, int height, boolean fliph, boolean flipv) {

        tileHeight = height;
        tileWidth = width;

        sprites = new ArrayList<>();

        try {
            loadSpritesCommon(path, fliph, flipv);
        } catch (IOException e) {
            System.err.println("SpriteSheet: Error while trying to cut "+path+" sprite sheet.");
        }


    }

    /**
     * Load image. Using in parse objects.
     * @// TODO: 15.01.2017 Need change SpriteSheet constructors
     * @param path SpriteSheetPath
     * @throws IOException
     */
    public void setImage(String path) throws IOException {
        image = ImageIO.read(new File(path));
    }

    /**
     *
     */
    public Sprite loadSprite(int spriteX, int spriteY, int width, int height) {
        BufferedImage subImage = image.getSubimage(spriteX, spriteY, width, height);
//                System.out.println("subImage x:" + subImage.getWidth() + " h: " + subImage.getHeight());
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
        return sprite;
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
        System.out.println(image.getWidth()/32 + " " + image.getHeight()/32);

        int width = 32;
        int height = 32;
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        int index = 0;
        for (int y = 0; y < imgHeight; y += height) {
            for (int x = 0; x < imgWidth; x += width, index++) {
//                System.out.println("x: " + x + " y: " + y + " w: " + width + " h: " + height);
                BufferedImage subImage = image.getSubimage(x, y, width, height);
//                System.out.println("subImage x:" + subImage.getWidth() + " h: " + subImage.getHeight());
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
//            System.out.println("Sprites column count = " + sprites.size());
        }
//        System.out.println("Sprites count = " + sprites.size());
    }

    /**
     * Unfortinately i can't use previous method, couse
     * tile width and height is hardcoded in there =(
     *
     * So it is corrent realisation, i think.
     *
     * @param path
     * @throws IOException
     */
    private void loadSpritesCommon(String path, boolean fliph, boolean flipv) throws IOException {

        BufferedImage spriteSheet = ImageIO.read(new File(path));

        int width = spriteSheet.getWidth();
        int height = spriteSheet.getHeight();

        for (int i = 0; i < height; i+= tileHeight) {
            for (int j = 0; j < width; j+= tileWidth) {
                BufferedImage nextTile = spriteSheet.getSubimage(j, i, tileWidth, tileHeight);

                sprites.add(new Sprite(nextTile, tileWidth, tileHeight, fliph, flipv));
            }
        }

    }

    public int getSpriteTextureId(int index) {
        return sprites.get(index).getTextureId();
    }

    public int size() {
        return sprites.size();
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

}
