package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Game;
import com.p3k.magictale.game.Characters.CharacterTypes;

import java.util.ArrayList;

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

    /**
     * current state of this character
     *
     */
    private int currentState;

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

        float cameraX = Game.getInstance().getCameraX();
        float cameraY = Game.getInstance().getCameraY();
    }

    /**
     * Just abstrat method for future realisations
     */
    public void processInput() {

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


}
