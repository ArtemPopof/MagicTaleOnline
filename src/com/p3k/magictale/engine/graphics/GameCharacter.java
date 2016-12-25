package com.p3k.magictale.engine.graphics;

import client.ClientGame;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Characters.CharacterTypes;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * GameCharacter class represent any living thing.
 * NPC or Players classes should be derived from
 * this one.
 *
 * Created by artem96 on 06.12.16.
 */
public class GameCharacter extends GameObject implements Serializable {

    protected static final int WAITING_STATE = 0;
    protected static final int RIGHT_MOVE_STATE = 1;
    protected static final int LEFT_MOVE_STATE =  2;
    protected static final int UP_MOVE_STATE = 3;
    protected static final int DOWN_MOVE_STATE = 4;
    protected static final int DEATH_STATE = 5;
    protected static final int LEFT_ATTACK_STATE = 6;
    protected static final int UP_ATTACK_STATE = 7;
    protected static final int RIGHT_ATTACK_STATE = 8;
    protected static final int DOWN_ATTACK_STATE = 9;

    /**
     * Length of full hp bar
     *
     * in pixels.
     *
     */
    private static final int FULL_HP_BAR_LENGTH = 40;

    /**
     * HP Bar height
     */
    private static final int HP_BAR_HEIGHT = 5;

    /**
     * How many pixels will be between character
     * and hp bar
     */
    private static final int HP_BAR_PADDING = 10;

    /**
     * Unique identifier for current class.
     * for derived classes it value must be changed.
     *
     * exmpl: for Blood Mage class it might be some-
     * thing like BLOOD_MAGE_CHARACTER_ID = 59;
     */
    protected CharacterTypes type = CharacterTypes.ABSTRACT_CHARACTER;

    protected ArrayList<Animation> animations;
    protected boolean isAttacking = false;
    /**
     * HpBar Sprite
     */
    protected Sprite hpBar;
    protected boolean isHPBarVisible;
    private float speed;
    private int health;
    private int maxHealth;
    private int attack;
    /**
     * How far can damage other character
     */
    private int attackDistance;
    private boolean isFlyable;
    private int layer;
    /**
     * current state of this character
     *
     */
    private int currentState;

    private boolean isDead = false;

    /**
     * Basic constructor for GameCharacter
     * @param x
     * @param y
     * @param width
     * @param height
     */

    public GameCharacter(float x, float y, float width, float height) {

        super(x, y, width, height);

        this.currentState = WAITING_STATE;

        try {
            this.animations = ResourceManager.getInstance().getAnimations(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO remove hardcode
        this.speed = Constants.PLAYER_SPEED;
        this.health = 10;
        this.maxHealth = 10;
        this.attack = 2;
        this.attackDistance = Constants.MAP_TILE_SIZE + 20;

        this.isFlyable = false;
        this.layer = 1;

        //init hp bar
        this.hpBar = new Sprite(1f, 0f, 0f, FULL_HP_BAR_LENGTH, HP_BAR_HEIGHT);
        this.isHPBarVisible = true;
    }

    public int getState() {
        return this.currentState;
    }

    public void setState(int state) {
        this.currentState = state;
    }

    @Override
    public void update() {

        // Animation must be performed here

        // next animation frame
        this.sprite = this.animations.get(this.currentState).update();

        // current animation stops, so zero some
        // states
        if (!this.animations.get(this.currentState).isRunning()) {
            this.isAttacking = false;
        }

        float cameraX = ClientGame.getInstance().getCameraX();
        float cameraY = ClientGame.getInstance().getCameraY();
    }

    /**
     * Just abstrat method for future realisations
     */
    public void processInput() {

    }

    /**
     * render character
     */
    public void render() {
        super.render();

        if (this.isHPBarVisible)
            renderHPBar();
    }

    /**
     * Render HP Bar on top of char
     */
    private void renderHPBar() {
        glPushMatrix();
        {
            float cameraX = ClientGame.getInstance().getCameraX();
            float cameraY = ClientGame.getInstance().getCameraY();

            this.hpBar.setWidth((getCurrentHealth() / (1.0f * this.maxHealth)) * FULL_HP_BAR_LENGTH);

            glTranslatef((this.x - cameraX) + getWidth() / 4, this.y - cameraY + HP_BAR_PADDING, 0);

            this.hpBar.render();

        }
        glPopMatrix();
    }

    /**
     * Returns unique, class
     * based ID, required for Resource Manager
     * and other stuff
     *
     * @return classID
     */
    public CharacterTypes getCharacterId() {
        return this.type;
    }

    /**
     * Change state if current state is different
     *
     * @param state
     */
    public void changeState(int state) {
        if (this.currentState != state) {
            this.currentState = state;
            this.animations.get(this.currentState).startOver();
        }
        this.animations.get(this.currentState).resume();
    }

    public float getSpeed() {
        return this.speed;
    }

    public int getCurrentHealth() {
        return this.health;
    }

    // mag = magnitude
    // move player according to given params
    protected void move(float magX, float magY) {

        float deltaX = magX * getSpeed();
        float deltaY = magY * getSpeed();

        this.x += deltaX;
        this.y += deltaY;

    }

    /**
     * Is this person able to fly
     */
    public boolean isFlyable() {
        return this.isFlyable;
    }

    /**
     * Return a layer index for this char
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Perform attack action
     *
     */
    public void doAttack() {

        Point currentPosition = new Point((int) getRealX(), (int) getRealY());

        switch (this.direction) {
            case LEFT:
                changeState(LEFT_ATTACK_STATE);

                break;
            case RIGHT:
                changeState(RIGHT_ATTACK_STATE);

                break;
            case UP:
                changeState(UP_ATTACK_STATE);

                break;
            case DOWN:
                changeState(DOWN_ATTACK_STATE);

                break;
        }

        GameCharacter potencialEnemy;

        ArrayList<GameCharacter> enemies = ClientGame.getInstance().getCharactersNearPoint(currentPosition, this.attackDistance);


        if (!this.isAttacking) {

            for(GameCharacter character : enemies) {
                if (character.equals(this)) continue;

                character.takeHarm(this.attack);
                break;
            }

        }


        this.isAttacking = true;
    }

    /**
     * Is HP Bar is visible
     */
    public boolean isHPBarVisible() {
        return this.isHPBarVisible;
    }

    /**
     * Set visibility for hp bar
     */
    public void setHPBarVisible(boolean visibility) {
        this.isHPBarVisible = visibility;
    }

    /**
     * Set character health
     */
    public void setHealth(int health) {
        this.health = health > 0 ? health : 0;
    }

    /**
     * Decrease HP of the Character
     *
     * @param attackStrength
     */
    public void takeHarm(int attackStrength) {
        // isn't sofisticated yet
        this.health -= attackStrength;

        if (this.health <= 0) {
            playDeath();
            this.health = 0;
        }
    }

    /**
     * We have no will to live, so
     * remove ourself from this game
     */
    public void playDeath() {

        setState(DEATH_STATE);
        this.isDead = true;
    }

    /**
     * return attack power of the character
     */
    public int getAttack() {
        return this.attack;
    }

    /**
     * return is character alive or dead
     */
    public boolean isDead() {
        return this.isDead;
    }

}
