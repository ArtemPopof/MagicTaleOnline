package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.Objects.AnimationSheetResource;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Created by artem96 on 03.12.16.
 */
public class Animation implements Constants, Serializable {

    private ArrayList<Frame> frames;
    private int currentFrame;

    /**
     * is animation running
     */
    private boolean isRunning;

    /**
     * animation will be repeted when played
     */
    private boolean isLooped = true;

    /**
     * if animation is played once it will set
     * frame to 0
     */
    private boolean shouldStartOver = true;

    public Animation(AnimationSheetResource sprites, int horizontalTileOffset, int framesCount) {

        frames = new ArrayList<>(framesCount);

        for (int i = horizontalTileOffset; i < framesCount + horizontalTileOffset; i++) {

            Frame nextFrame = new Frame(sprites.getSprites().get(i), sprites.getSpriteId(i), FRAME_SHOW_COUNT);

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
                if (currentFrame == 0 && !isLooped) {
                    // animation played once
                    isRunning = false;

                    if (!shouldStartOver) {
                        currentFrame = frames.size() -1;
                    }
                }
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

    /**
     * Set loop option to the animation.
     * If looped is true, then animation
     * will be played, until something
     * stops it.
     * If looped if false, then animation
     * will be played once.
     */
    public void setLooped(boolean looped) {
        this.isLooped = looped;
    }

    public boolean isLooped() {
        return isLooped;
    }

    /**
     * is animation not paused
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Is animation should start over when played
     */
    public boolean isShouldStartOver() {
        return shouldStartOver;
    }

    /**
     * sets should start over var
     */
    public void setShouldStartOver(boolean should) {
        this.shouldStartOver = should;
    }


}
