package com.p3k.magictale.game.Characters;

import client.ClientGame;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.enums.Direction;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.physics.Collision;
import org.lwjgl.input.Keyboard;

import java.io.Serializable;

/**
 * Created by artem96 on 04.12.16.
 */
public class Player extends GameCharacter implements Constants, Serializable {


    //private SoundSource mainSound;
    //private SoundSource attackSound;

    public Player(float x, float y) {

        super(x, y, 78, 112);

        this.type = CharacterTypes.ABSTRACT_PLAYER;

        try {
            this.animations = ResourceManager.getInstance().getAnimations(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         try {
         this.sprite = new Sprite("res/hp_bar.png", 216, 34);
         } catch (IOException e) {
         System.err.println("Cannot init sprite for player class");
         System.err.println(e.getMessage());
         System.exit(1);
         }**/

        // this.sprite = new Sprite(0.5f, 0.5f, 0.5f, 60, 60);

        this.health = PLAYER_START_HP;
        this.maxHealth = PLAYER_START_HP;

        this.attack = PLAYER_START_ATTACK;

        //initSounds();

        //SoundManager.getInstance().setListenerPos(10.0f, 10.0f);
    }

    /*private void initSounds() {

        try {
            mainSound = new SoundSource(null, true);
            attackSound = new SoundSource(null, false);
            attackSound.setLevel(0.8f);
            mainSound.setLevel(0.3f);
        } catch (Exception e) {
            System.err.println("Error initializing sound for player!");
        }

        if (mainSound != null) {
            mainSound.play("user/baphomet_breath.wav");
        }

    }*/

    @Override
    public void update() {
        super.update();

        System.out.println("EXP: " + getXp());
        System.out.println("LVL: " + getLevel());
        System.out.println("MAXHP: " + getMaxHealth());

        //TODO do not left it undone
        this.x = 800 / 2 + ((ClientGame) ClientGame.getInstance()).getCameraX();
        this.y = 600 / 2 + ((ClientGame) ClientGame.getInstance()).getCameraY();

    }

    public void processInput() {

        if (isDead()) {
            System.out.println("WASTED BUDDY!");
            return;
        }

        boolean isStateChanged = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            changeState(UP_MOVE_STATE);
            move(0, 1);
            setDirection(Direction.UP);
            isStateChanged = true;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            changeState(DOWN_MOVE_STATE);
            move(0, -1);
            setDirection(Direction.DOWN);
            isStateChanged = true;
        } else {

            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                changeState(LEFT_MOVE_STATE);
                move(-1, 0);
                setDirection(Direction.LEFT);
                isStateChanged = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                changeState(RIGHT_MOVE_STATE);
                move(1, 0);
                setDirection(Direction.RIGHT);
                isStateChanged = true;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            //attackSound.play("user/attack_axe.wav");
        }

        // mouse events
        if (((ClientGame) ClientGame.getInstance()).isMouseReleased()) {
            doAttack();
        }

        if (this.isAttacking) {
            isStateChanged = true;
        }

        // if nothing happens with player, then wait
        if (!isStateChanged) {
            this.animations.get(getState()).pause();
        }

    }

    // mag = magnitude
    // move player according to given params
    protected void move(float magX, float magY) {

        if (this.isAttacking)
            this.isAttacking = false;

        float deltaX = magX * getSpeed();
        float deltaY = magY * getSpeed();

        //  x += deltaX;
        //  y += deltaY;

        float oldX = ((ClientGame) ClientGame.getInstance()).getCameraX();
        float oldY = ((ClientGame) ClientGame.getInstance()).getCameraY();

        this.setX(this.x + deltaX);
        this.setY(this.y + deltaY);

        if (Collision.checkForCollision(this)) {
            // freeze! collision!
            this.setX(this.x - deltaX);
            this.setY(this.y - deltaY);
        } else {
            ((ClientGame) ClientGame.getInstance()).setCameraX(oldX + deltaX);
            ((ClientGame) ClientGame.getInstance()).setCameraY(oldY + deltaY);
        }


    }

}
