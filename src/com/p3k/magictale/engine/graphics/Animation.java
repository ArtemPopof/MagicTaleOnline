package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Constants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by artem96 on 03.12.16.
 */
public class Animation implements Constants {

    private ArrayList<Frame> frames;
    private int currentFrame;

    public Animation(ArrayList<Sprite> sprites) {

        frames = new ArrayList<>();

        for (Sprite sprite : sprites) {
            frames.add(new Frame(sprite, 1));
        }

    }

    /**
     * Creates animation based on animationSpriteSheet path,
     * hor. tile offset and frameCount for this Animation.
     *
     * Test constructor
     *
     * @param pathToAnimationSheet - path to sprite sheet with animation sprites
     * @param framesCount - frame count for this animation
     * @param width  - width of frame
     * @param height - height of frame
     * */
    public Animation(String pathToAnimationSheet,
                     int framesCount, int width, int height) {

        frames = new ArrayList<>(framesCount);

        try {

            // @TODO Animation class
            // this constructor under development,
            // so it doesn't work properly now.
            for (int i = 0; i < framesCount; i++) {

                Sprite nextSprite = new Sprite(pathToAnimationSheet + i + ".png",
                        width, height);

                Frame nextFrame = new Frame(nextSprite,FRAME_SHOW_COUNT);

                frames.add(nextFrame);
            }

        } catch (IOException e) {
            System.err.println("Cannot load sprite "+pathToAnimationSheet + ".png");
            e.printStackTrace();
        }


        currentFrame = 0;

    }

    public Animation(String pathToAnimationSheet,
                     int horizontalTileOffset,
                     int framesCount, int tileWidth, int tileHeight) {

        frames = new ArrayList<>(framesCount);


        SpriteSheet animationSprites = new SpriteSheet(pathToAnimationSheet, tileWidth, tileHeight);

        ArrayList<Sprite> sprites = animationSprites.getSprites();

        for (Sprite nextSprite : sprites) {

            Frame nextFrame = new Frame(nextSprite, FRAME_SHOW_COUNT);

            frames.add(nextFrame);

        }

    }

    public Animation(SpriteSheet sprites, int horizontalTileOffset, int framesCount) {

        frames = new ArrayList<>(framesCount);

        ArrayList<Sprite> tileSprites = sprites.getSprites();

        for (int i = horizontalTileOffset; i < framesCount + horizontalTileOffset; i++) {

            Frame nextFrame = new Frame(tileSprites.get(i), FRAME_SHOW_COUNT);

            frames.add(nextFrame);

        }
    }

    public Sprite update() {
        Frame frame = frames.get(currentFrame);

        if (!frame.update()) {
            currentFrame++;
            currentFrame %= frames.size();
        }

        return frame.getSprite();
    }
}
