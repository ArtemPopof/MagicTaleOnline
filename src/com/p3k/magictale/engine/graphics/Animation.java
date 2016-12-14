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

    /**
     * is animation running
     */
    private boolean isRunning;

    public Animation(ArrayList<Sprite> sprites) {

        frames = new ArrayList<>();

        for (Sprite sprite : sprites) {
            frames.add(new Frame(sprite, 1));
        }

        isRunning = true;
        currentFrame = 0;

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
        isRunning = true;
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

        isRunning = true;
        currentFrame = 0;

    }

    public Animation(SpriteSheet sprites, int horizontalTileOffset, int framesCount) {

        frames = new ArrayList<>(framesCount);

        ArrayList<Sprite> tileSprites = sprites.getSprites();

        for (int i = horizontalTileOffset; i < framesCount + horizontalTileOffset; i++) {

            Frame nextFrame = new Frame(tileSprites.get(i), FRAME_SHOW_COUNT);

            frames.add(nextFrame);

        }

        isRunning = true;
        currentFrame = 0;
    }

    public Sprite update() {

        Frame frame = frames.get(currentFrame);

        // if animation is not paused,
        // show next frame
        if (isRunning) {
            if (!frame.update()) {
                currentFrame++;
                currentFrame %= frames.size();
            }
        }

        return frame.getSprite();
    }

    /**
     * Stops frames changing.
     * Don't forget to use setFrame()
     * to pick a frame to freeze
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * resume animation
     */
    public void resume() {
        isRunning = true;
    }

    /**
     * Set a next frame of animation.
     *
     * @param frame - must be valid value (< frames.size() and >= 0);
     */
    public void setFrame(int frame)  {

        if (frame >= frames.size() || frame < 0) {
            System.err.println("Animation.setFrame("+frame+"): Invalid argument");
        } else {
            this.currentFrame = frame;
        }
    }

    /**
     * Start animation from first frame
     *
     */
    public void startOver() {
        setFrame(0);
        resume();
    }
}
