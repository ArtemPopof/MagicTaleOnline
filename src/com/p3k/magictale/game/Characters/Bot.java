package com.p3k.magictale.game.Characters;

import com.p3k.magictale.engine.graphics.GameCharacter;

/**
 *
 * Just player class with auto-pilot mode =)
 *
 *
 * Created by artem96 on 12.12.16.
 */
public class Bot extends GameCharacter {

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

    public Bot(float x, float y, float width, float height) {
        super(x, y, width, height);

        // 10 keys should be enough, i think
        // all values equal false?
        keysState = new boolean[EMULATED_KEYS];
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
}
