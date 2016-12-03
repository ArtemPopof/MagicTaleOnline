package com.p3k.magictale.graphics;

import java.util.ArrayList;

/**
 * Created by artem96 on 03.12.16.
 */
public class Animation {

    private ArrayList<Frame> frames;
    private int currentFrame;

    public Animation() {
        frames = new ArrayList<>();
    }

    public void render() {
        Frame frame = frames.get(currentFrame);

        if (frame.render()) {
            currentFrame++;
            currentFrame %= frames.size();
        }
    }
}
