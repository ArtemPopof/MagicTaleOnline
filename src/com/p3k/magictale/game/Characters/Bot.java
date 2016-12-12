package com.p3k.magictale.game.Characters;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;

import java.util.Random;

/**
 *
 * Just player class with auto-pilot mode =)
 *
 *
 * Created by artem96 on 12.12.16.
 */
public class Bot extends GameCharacter {

    /**
     * Bot states
     */

    /**
     * Just waiting a waitTime period of time
     */
    private static final int BOT_WAITING_STATE = 0;

    /**
     * nothing to do, so move to the random cell in your
     * vision radius
     */
    private static final int BOT_PATRULING_STATE = 1;

    /**
     * Bot has the target, so find a way to destroy it
     */
    private static final int BOT_TARGETSPOTED_STATE = 2;


    private int currentBotState;

    /**
     * How many keyboard keys will be emulated
     *
     */
    private static final int EMULATED_KEYS = 10;

    /**
     * Keys for emulation of pressing
     */
    private static final int KEY_W = 0;
    private static final int KEY_S = 1;
    private static final int KEY_A = 2;
    private static final int KEY_D = 3;

    private static final int KEY_SPACE = 4;

    private boolean[] keysState;

    // bot can see you if you
    // in this imaginary circle
    private int visionRadius;

    /**
     * If not aggressive, then bot
     * will be attack you only if
     * you trying to hurt him.
     *
     */
    private boolean isAggressive;

    /**
     * Just a random generator
     */
    private Random random;

    /**
     * Patruling mode variables
     */

    private float destinationX;
    private float destinationY;

    /**
     * Waiting mode variables
     */

    private int framesToWait;


    public Bot(float x, float y, float width, float height) {
        super(x, y, width, height);

        // 10 keys should be enough, i think
        // all values equal false?
        keysState = new boolean[EMULATED_KEYS];

        visionRadius = 50;
        isAggressive = true;

        currentBotState = BOT_PATRULING_STATE;

        random = new Random(System.currentTimeMillis());

        destinationX = x;
        destinationY = y;

        framesToWait = -1;
    }

    /**
     * Emulation of real-player
     * input reaction. Bot simulating
     * key press with boolean global
     * variables like isKeyDown(int KEY)
     */
    public void processInput() {

        try {

            boolean isSomethingHappens = false;

            if (isKeyDown(KEY_W)) {
                changeState(UP_MOVE_STATE);
                move(0, 1);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_S)) {
                changeState(DOWN_MOVE_STATE);
                move(0, -1);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_A)) {
                changeState(LEFT_MOVE_STATE);
                move(-1, 0);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_D)) {
                changeState(RIGHT_MOVE_STATE);
                move(1, 0);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_SPACE)){
                // nothing's here yet
            }

            if (!isSomethingHappens) {
                setState(WAITING_STATE);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {

        super.update();

        // here will be simulation of bot virtual brain

        if (currentBotState == BOT_PATRULING_STATE) {
            doPatroling();
        } if (currentBotState == BOT_WAITING_STATE) {
            boolean isEnoughWaiting = doWait();

            if (isEnoughWaiting) {
                setState(BOT_PATRULING_STATE);
            }
        }

    }

    /**
     * Just walk around the current spot in
     * random manner
     */
    private void doPatroling() {

        if (destinationY == -1) {
            // state has been changed just now
            // so generate random destination to follow

            int randomX = random.nextInt(visionRadius);
            int randomY = random.nextInt(visionRadius);

            destinationY = getY() + randomY;
            destinationX = getX() + randomX;

            //here will be collision check, so we can be
            //sure that destination is reachable

        }else if ((getX() - destinationX) <= 5 && (getY() - destinationY) <= 5) {
            // if bot come pretty close to it's destination point

            setBotState(BOT_WAITING_STATE);
            destinationX = -1;
            destinationY = -1;
        } else {

            // move to our target
            // a* algorithm?

        }

    }

    /**
     * Waiting another frame. And decrement waitFrames variable.
     * If it's = zero, then return true.
     */
    private boolean doWait() {

        // if it's first frame to wait
        // so decide, how long he's going to wait
        if (framesToWait == -1) {
            framesToWait = random.nextInt(5 * Constants.MAX_FPS);
        } else if (framesToWait > 0) {
            // waiting another frame
            framesToWait--;
        } else {
            // enough waiting
            setState(BOT_PATRULING_STATE);
            framesToWait = -1;
            return true;
        }

        return false;
    }



    /**
     * Check if virtual bot pressed the given key
     *
     * @param keyCode - key being checked
     * @return - true or false
     * @throws IllegalArgumentException - if keyCode is incorrect
     */
    private boolean isKeyDown(int keyCode) throws IllegalArgumentException {

        if (keyCode < 0 || keyCode >= EMULATED_KEYS) {
            throw new IllegalArgumentException("isKeyDown: unknown keyCode");
        }
        return keysState[keyCode];
    }

    /**
     * Set bot state
     */

    private void setBotState(int state) {
        currentBotState = state;
    }
}
