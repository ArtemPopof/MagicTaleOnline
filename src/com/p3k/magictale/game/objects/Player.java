package com.p3k.magictale.game.objects;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.IOException;

/**
 * Created by artem96 on 04.12.16.
 */
public class Player extends GameObject implements Constants{

    private float speed;
    private int health;
    private float xp;

    private SoundSource mainSound;
    private SoundSource attackSound;


    public Player(float x, float y) {
       this.x = x;
       this.y = y;


       try {
           this.sprite = new Sprite("res/hp_bar.png", 216, 34);
       } catch (IOException e) {
           System.err.println("Cannot init sprite for player class");
           System.err.println(e.getMessage());
           System.exit(1);
       }

      // this.sprite = new Sprite(0.5f, 0.5f, 0.5f, 60, 60);
       xp = 0;
       health = 10;
       speed = 4f;

        try {
            mainSound = new SoundSource(null, true);
            attackSound = new SoundSource(null, false);
            attackSound.setLevel(25.0f);
        } catch (Exception e) {
            System.err.println("Error initializing sound for player!");
        }

        if ( mainSound != null ) {
            mainSound.play("user/baphomet_breath.wav");
        }

        //SoundManager.getInstance().setListenerPos(10.0f, 10.0f);
    }

    @Override
    public void update() {
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

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            attackSound.play("user/attack_axe.wav");
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
