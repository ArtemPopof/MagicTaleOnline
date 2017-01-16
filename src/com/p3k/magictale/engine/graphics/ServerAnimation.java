package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.graphics.Animation;
import com.p3k.magictale.engine.graphics.Objects.ServerSheetInfo;

/**
 * Animation class realization for server use
 * Don't contain sprites, using spriteId instead
 *
 * Created by artem96 on 16.01.17.
 */
public class ServerAnimation extends Animation {

    public ServerAnimation(AnimationInfo sprites, int horizontalTileOffset, int framesCount) {

        super(framesCount);

        for (int i = horizontalTileOffset; i < framesCount + horizontalTileOffset; i++) {

            VirtualFrame nextFrame =
                    new VirtualFrame(sprites.getSpriteId(i), FRAME_SHOW_COUNT);

            frames.add(nextFrame);

        }

    }

    /**
     * return next spriteId
     * @return
     */
    public int update() {
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

        return frame.getSpriteId();
    }


}
