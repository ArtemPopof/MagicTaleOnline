package com.p3k.magictale.game.objects;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Sprite;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by artem96 on 04.12.16.
 */
public class Player extends GameObject implements Constants{

    private float speed;
    private int health;
    private float xp;

    public Player(float x, float y) {
       this.x = x;
       this.y = y;

       try {
           this.sprite = new Sprite("res/bird.png", 128, 128);
       } catch (IOException e) {
           System.err.println("Cannot init sprite for player class");
           System.err.println(e.getMessage());
           System.exit(1);
       }
       xp = 0;
       health = 10;
       speed = 4f;
    }

    @Override
    public void update() {
        System.out.println("SPEED: " + speed);
        System.out.println("LEVEL: " + getLevel());
        System.out.println("XP: " + xp);
        System.out.println("STRENGTH: " + getStrength());
        System.out.println("MAGIC: " + getMagic());
    }

    public void processInput() {

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            move(0, 1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            move(0, -1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            move(-1, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
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

    public float getXp() {
        return xp;
    }

    public void addXp(float amount) {
        xp += amount;
    }

    public int getLevel() {
        return (int) (xp / 50) + 1;
    }

    public int getMaxHealth() {
        return getLevel() * 10;
    }

    public int getStrength() {
        return (int) (getLevel() * 4f);
    }

    public int getMagic() {
        return (int) (getLevel() * 1.5f);
    }

    public int getCurrentHealth() {

        int maxHealth = getMaxHealth();

        if (health > maxHealth) {
            health = maxHealth;
        }

        return health;
    }
}
