package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.game.objects.CharacterTypes;

import java.util.ArrayList;
import java.util.Map;

/**
 * GameCharacter class represent any living thing.
 * NPC or Players classes should be derived from
 * this one.
 *
 * Created by artem96 on 06.12.16.
 */
public class GameCharacter extends GameObject {

    protected static final int WAITING_STATE = 0;

    /**
     * Unique identifier for current class.
     * for derived classes it value must be changed.
     *
     * exmpl: for Blood Mage class it might be some-
     * thing like BLOOD_MAGE_CHARACTER_ID = 59;
     */
    protected CharacterTypes type = CharacterTypes.ABSTRACT_CHARACTER;

    protected ArrayList<Animation> animations;

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


}