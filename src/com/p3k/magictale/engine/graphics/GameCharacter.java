package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Game;
import com.p3k.magictale.game.Characters.CharacterTypes;
import com.p3k.magictale.map.level.LevelManager;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * GameCharacter class represent any living thing.
 * NPC or Players classes should be derived from
 * this one.
 *
 * Created by artem96 on 06.12.16.
 */
public class GameCharacter extends GameObject {

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

    private float speed;
    private int health;
    private int maxHealth;
    private int attack;

    private boolean isFlyable;
    private int layer;

    protected boolean isAttacking = false;

    /**
     * HpBar Sprite
     */
    protected Sprite hpBar;

    protected boolean isHPBarVisible;

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

        super.init(x, y, width, height);

        currentState = WAITING_STATE;

        try {
            animations = ResourceManager.getInstance().getAnimations(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO remove hardcode
        speed = Constants.PLAYER_SPEED;
        health = 10;
        maxHealth = 10;
        attack = 2;

        isFlyable = false;
        layer = 1;

        //init hp bar
        hpBar = new Sprite(1f, 0f, 0f, FULL_HP_BAR_LENGTH, HP_BAR_HEIGHT);
        isHPBarVisible = true;
    }

    public int getState() {
        return currentState;
    }

    public void setState(int state) {
        currentState = state;
    }

    @Override
    public void update() {

        // Animation must be performed here

        // next animation frame
        this.sprite = animations.get(currentState).update();

        // current animation stops, so zero some
        // states
        if (!animations.get(currentState).isRunning()) {
            isAttacking = false;
        }

        float cameraX = Game.getInstance().getCameraX();
        float cameraY = Game.getInstance().getCameraY();
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

        if (isHPBarVisible)
            renderHPBar();
    }

    /**
     * Render HP Bar on top of char
     */
    private void renderHPBar() {
        glPushMatrix();
        {
            float cameraX = Game.getInstance().getCameraX();
            float cameraY = Game.getInstance().getCameraY();

            hpBar.setWidth((getCurrentHealth() / (1.0f * maxHealth)) * FULL_HP_BAR_LENGTH);

            glTranslatef((x - cameraX) + getWidth() / 4, y - cameraY + HP_BAR_PADDING, 0);

            hpBar.render();

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
        return type;
    }

    /**
     * Change state if current state is different
     *
     * @param state
     */
    public void changeState(int state) {
        if (currentState != state) {
            currentState = state;
            animations.get(currentState).startOver();
        }
        animations.get(currentState).resume();
    }

    public float getSpeed() {
        return speed;
    }

    public int getCurrentHealth() {
        return health;
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
        return isFlyable;
    }

    /**
     * Return a layer index for this char
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Perform attack action
     *
     */
    public void doAttack() {

        Point cellToAttack = new Point();

        Point currentCell = LevelManager.getTilePointByCoordinates(getRealX(), getRealY());

        switch (direction) {
            case LEFT:
                changeState(LEFT_ATTACK_STATE);
                cellToAttack = new Point(currentCell.x - 1, currentCell.y);
                break;
            case RIGHT:
                changeState(RIGHT_ATTACK_STATE);
                cellToAttack = new Point(currentCell.x + 1, currentCell.y);
                break;
            case UP:
                changeState(UP_ATTACK_STATE);
                cellToAttack = new Point(currentCell.x, currentCell.y - 1);
                break;
            case DOWN:
                changeState(DOWN_ATTACK_STATE);
                cellToAttack = new Point(currentCell.x, currentCell.y + 1);
                break;
        }

        GameCharacter potencialEnemy;

        // if someone in attack distance
        if ((potencialEnemy = Game.getInstance().getAnyoneInCell(cellToAttack.x, cellToAttack.y)) != null) {
            if (!isAttacking) {
                // if first frame of attack is playing
                potencialEnemy.takeHarm(getAttack());
            }
        }


        isAttacking = true;
    }

    /**
     * Is HP Bar is visible
     */
    public boolean isHPBarVisible() {
        return isHPBarVisible;
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

        if (health <= 0) {
            playDeath();
            health = 0;
        }
    }

    /**
     * We have no will to live, so
     * remove ourself from this game
     */
    public void playDeath() {

        setState(DEATH_STATE);
        isDead = true;
    }

    /**
     * return attack power of the character
     */
    public int getAttack() {
        return attack;
    }

    /**
     * return is character alive or dead
     */
    public boolean isDead() {
        return isDead;
    }

}
