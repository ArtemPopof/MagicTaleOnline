package com.p3k.magictale.game.Characters;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.Game;
import org.lwjgl.input.Keyboard;

/**
 * Created by artem96 on 04.12.16.
 */
public class Player extends GameCharacter implements Constants{

    private float xp;

    private SoundSource mainSound;
    private SoundSource attackSound;


    public Player(float x, float y) {

        super(x, y, 78, 112);

        type = CharacterTypes.ABSTRACT_PLAYER;

        try {
            animations = ResourceManager.getInstance().getAnimations(this);
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

        xp = 0;

        initSounds();

        //SoundManager.getInstance().setListenerPos(10.0f, 10.0f);
    }

    private void initSounds() {

        try {
            mainSound = new SoundSource(null, true);
            attackSound = new SoundSource(null, false);
            attackSound.setLevel(0.8f);
            mainSound.setLevel(0.3f);
        } catch (Exception e) {
            System.err.println("Error initializing sound for player!");
        }

        if ( mainSound != null ) {
            mainSound.play("user/baphomet_breath.wav");
        }

    }

    @Override
    public void update() {
        super.update();

        //TODO do not left it undone
        this.x = 800/2 + Game.getInstance().getCameraX();
        this.y = 600/2 + Game.getInstance().getCameraY();
    }

    public void processInput() {

        boolean isStateChanged = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            changeState(UP_MOVE_STATE);
            move(0, 1);
            isStateChanged = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            changeState(DOWN_MOVE_STATE);
            move(0, -1);
            isStateChanged = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            changeState(LEFT_MOVE_STATE);
            move(-1, 0);
            isStateChanged = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            changeState(RIGHT_MOVE_STATE);
            move(1, 0);
            isStateChanged = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            attackSound.play("user/attack_axe.wav");
        }

        // if nothing happens with player, then wait
        if (!isStateChanged) {
            changeState(WAITING_STATE);
        }

    }

    // mag = magnitude
    // move player according to given params
    protected void move(float magX, float magY) {

        float deltaX = magX * getSpeed();
        float deltaY = magY * getSpeed();

        //  x += deltaX;
        //  y += deltaY;

        float oldX = Game.getInstance().getCameraX();
        float oldY = Game.getInstance().getCameraY();

        Game.getInstance().setCameraX(oldX + deltaX);
        Game.getInstance().setCameraY(oldY + deltaY);

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


}
