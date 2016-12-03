package com.p3k.magictale.game.objects;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Sprite;
import org.lwjgl.input.Keyboard;

/**
 * Created by artem96 on 04.12.16.
 */
public class Player extends GameObject implements Constants{

    private float speed;

    public Player(float x, float y) {
       init(x, y, 0.1f, 0.1f, 0.5f, PLAYER_SIZE, PLAYER_SIZE);

       speed = 4f;
    }

    public void processInput() {

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            move(0, 1);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            move(0, -1);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            move(-1, 0);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            move(1, 0);
        }
    }

    // mag = magnitude
    // move player according to given params
    private void move(float magX, float magY) {

        x += magX * getSpeed();
        y += magY * getSpeed();

    }

    public float getSpeed() {
        return speed;
    }
}
