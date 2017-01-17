package com.p3k.magictale.engine.graphics;

import client.ClientGame;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.enums.Direction;
import com.p3k.magictale.engine.gui.ComponentFactory;
import com.p3k.magictale.engine.physics.Collision;
import com.p3k.magictale.game.Characters.CharacterTypes;
import common.remoteInterfaces.GameController;
import server.ServerGame;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * GameCharacter class represent any living thing.
 * NPC or Players classes should be derived from
 * this one.
 * <p>
 * Created by artem96 on 06.12.16.
 */
public class GameCharacter extends GameObject implements Serializable {

    protected static final int WAITING_STATE = 0;
    protected static final int RIGHT_MOVE_STATE = 1;
    protected static final int LEFT_MOVE_STATE = 2;
    protected static final int UP_MOVE_STATE = 3;
    protected static final int DOWN_MOVE_STATE = 4;
    protected static final int DEATH_STATE = 5;
    protected static final int LEFT_ATTACK_STATE = 6;
    protected static final int UP_ATTACK_STATE = 7;
    protected static final int RIGHT_ATTACK_STATE = 8;
    protected static final int DOWN_ATTACK_STATE = 9;
    /**
     * Length of full hp bar
     * <p>
     * in pixels.
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
    protected volatile State characterState;
    /**
     * Unique identifier for current class.
     * for derived classes it value must be changed.
     * <p>
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
    protected float speed;
    protected int health;
    protected int maxHealth;
    protected int attack;
    /**
     * How far can damage other character
     */
    private int attackDistance;
    private int attackPenalty;
    private boolean isFlyable;
    private int layer;
    /**
     * current state of this character
     */
    protected volatile int currentState;
    private boolean isDead = false;
    /**
     * Experience gained by killind this mob
     */
    private int gainedExperience;
    private int xp;
    private int currentLevel;
    private ComponentFactory guiFactory;
    /**
     * Harm was taken in previous update
     */
    private int takenHarm;

    /**
     * Basic constructor for GameCharacter
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param isAnimationEnabled - if false, then it's serverObject, dont render it
     **/
    public GameCharacter(float x, float y, float width, float height, boolean isAnimationEnabled) {
        super(x, y, width, height);

        characterState = State.WAIT;

        this.currentState = WAITING_STATE;

        attackPenalty = 0;

        if (isAnimationEnabled) {
            try {
                this.animations = (ArrayList<Animation>) ResourceManager.getInstance(true).getAnimations(this).clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            // use virtual animations instead

            this.animations = (ArrayList<Animation>) ResourceManager.getInstance(false).getServerAnimations(this).clone();

        }

        //TODO remove hardcode
        this.speed = Constants.PLAYER_SPEED;
        this.maxHealth = 10;
        this.health = maxHealth;
        this.attack = 2;
        this.attackDistance = Constants.MAP_TILE_SIZE + 20;

        this.gainedExperience = 2;
        this.xp = 0;

        this.isFlyable = false;
        this.layer = 1;

        //init hp bar
        this.hpBar = new Sprite(1f, 0f, 0f, FULL_HP_BAR_LENGTH, HP_BAR_HEIGHT);
        this.isHPBarVisible = true;

        this.currentLevel = 1;

        this.takenHarm = -1;

        //guiFactory = new StdComponentFactory();
    }

    public static GameCharacter createGameCharacter(float x, float y, float width, float height) {
        return new GameCharacter(x, y, width, height, true);
    }

    public static GameCharacter createServerCharacter(float x, float y, float width, float height) {
        return new GameCharacter(x, y, width, height, false);
    }

    public int getState() {
        return this.currentState;
    }

    public void setState(int state) {
        this.currentState = state;
    }

    /**
     * render character
     */
//    public void render() {
//        super.render();
//
//        if (this.isHPBarVisible)
//            renderHPBar();
//    }
    @Override
    public void update() {
        if (!isDead && characterState != State.WAIT && !this.animations.get(currentState).isRunning()) {
            this.animations.get(currentState).startOver();
        }
        // next animation frame
        this.spriteId = this.animations.get(this.currentState).update();

        this.animations.get(this.currentState).getCurrentFrame();

        // current animation stops, so zero some
        // states
        if (!this.animations.get(this.currentState).isRunning()) {
            this.isAttacking = false;
        }

        // movement
        /*
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
         */

        if (attackPenalty > 0) {
            attackPenalty -= 1;
        }

        switch (characterState) {
            case ATTACK:
                doAttack();
                break;
            case MOVE:
                switch (direction) {
                    case UP:
                        currentState = UP_MOVE_STATE;
                        move(0, 1);
                        break;
                    case LEFT:
                        currentState = LEFT_MOVE_STATE;
                        move(-1, 0);
                        break;
                    case RIGHT:
                        currentState = RIGHT_MOVE_STATE;
                        move(1, 0);
                        break;
                    default:
                        currentState = DOWN_MOVE_STATE;
                        move(0, -1);
                        break;
                }
                break;
            case WAIT:
                if (type == CharacterTypes.ABSTRACT_PLAYER && this.animations.get(currentState).isLooped()) {
                    this.animations.get(currentState).stop();
                }
            default:
                break;
        }

        //sync with server
        ((ServerGame) ServerGame.getInstance()).putServerObject(getId(), getSpriteId(), x, y);

        // float cameraX = ((ClientGame) ClientGame.getInstance()).getCameraX();
        // float cameraY = ((ClientGame) ClientGame.getInstance()).getCameraY();
    }

    /**
     * Just abstrat method for future realisations
     */
    public void processInput() {

    }

    /**
     * Render HP Bar on top of char
     */
    private void renderHPBar() {
        glPushMatrix();
        {
            float cameraX = ((ClientGame) ClientGame.getInstance()).getCameraX();
            float cameraY = ((ClientGame) ClientGame.getInstance()).getCameraY();

            this.hpBar.setWidth((getCurrentHealth() / (1.0f * this.maxHealth)) * FULL_HP_BAR_LENGTH);

            //if (takenHarm > -1) {

            //   Text hitText = guiFactory.createText("Hit: " + takenHarm, "regular");
            //    hitText.setSize(16);

            //    hitText.move((this.x - cameraX) + getWidth() /4, this.y - cameraY + HP_BAR_PADDING * 2);

            //    hitText.render();

            //      takenHarm = -1;
            // }

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

        if (Collision.checkForCollision(this)) {
            // freeze! collision!
            this.setX(this.x - deltaX);
            this.setY(this.y - deltaY);
        }
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

        ArrayList<GameCharacter> enemies = ((ServerGame) ServerGame.getInstance()).getCharactersNearPoint(currentPosition, this.attackDistance);


        if (this.isAttacking && attackPenalty == 0) {

            for (GameCharacter character : enemies) {
                if (character.equals(this)) continue;

                if (!character.isDead) {
                    character.takeHarm(this.attack, this);
                    break;
                }
            }

            attackPenalty = 20;

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
    public void takeHarm(int attackStrength, GameCharacter attacker) {
        // isn't sofisticated yet
        this.health -= attackStrength;

        if (this.health <= 0) {

            if (!isDead) {
                attacker.addXp(this.getGainedExperience());
            }

            playDeath();
            this.health = 0;

        } else {
            takenHarm = attackStrength;
        }
    }

    /**
     * We have no will to live, so
     * remove ourself from this game
     */
    public void playDeath() {
        if (this.isDead) {
            return;
        }

        setState(DEATH_STATE);
        this.isDead = true;
        this.characterState = State.DEATH;
    }

    /**
     * return attack power of the character
     */
    public int getAttack() {
        return this.attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * return is character alive or dead
     */
    public boolean isDead() {
        return this.isDead;
    }

    /**
     * How much expirience will take Player, if
     * kill this bot
     *
     * @return
     */
    public int getGainedExperience() {
        return gainedExperience;
    }

    public void setGainedExperience(int newXp) {
        this.gainedExperience = newXp;
    }

    public int getXp() {
        return this.xp;
    }

    public void addXp(int amount) {

        this.xp += amount;

        if (xp >= getExperienceForNextLevel()) {
            levelUp();
        }
    }

    public int getLevel() {
        return currentLevel;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getStrength() {
        return (int) (getLevel() * 4f);
    }

    public int getMagic() {
        return (int) (getLevel() * 1.5f);
    }

    /**
     * Get expirience amount for specific level
     *
     * @return
     */
    public int getExperienceForLevel(int level) {
        return level * 10;
    }

    public int getExperienceForNextLevel() {
        return (getLevel()) * 10;
    }

    /**
     * Hooray, next level! You have grown stronger!
     */
    public void levelUp() {
        this.currentLevel++;

        maxHealth += currentLevel * 5;
        attack += 1 * currentLevel;

        this.health = maxHealth;


    }

    /**
     * Set size for all sprites for this char
     * <p>
     * Can be cpu-consuming, so use only when init
     *
     * @param width
     * @param height
     */
    public void setCharacterSize(float width, float height) {
        this.setWidth(width);
        this.setHeight(height);

        for (Animation anim : animations) {
            anim.setFramesSize(width, height);
        }
    }

    public int getSpriteId() {
        return this.animations.get(currentState).getCurrentFrame().getSpriteId();
    }

    public void setCharacterState(GameController.State state) {
        switch (state) {
            case ATTACK:
                characterState = State.ATTACK;
                break;
            case MOVE:
                characterState = State.MOVE;
                break;
            case WAIT:
            default:
                characterState = State.WAIT;
                break;
        }
    }

    public void setDirection(GameController.Direction direction) {
        switch (direction) {
            case UP:
                this.direction = Direction.UP;
                break;
            case RIGHT:
                this.direction = Direction.RIGHT;
                break;
            case LEFT:
                this.direction = Direction.LEFT;
                break;
            case DOWN:
            default:
                this.direction = Direction.DOWN;
                break;
        }
    }

    protected enum State {
        WAIT,
        MOVE,
        ATTACK,
        DEATH
    }


}
