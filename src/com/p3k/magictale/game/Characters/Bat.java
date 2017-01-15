package com.p3k.magictale.game.Characters;

import com.p3k.magictale.engine.graphics.Animation;
import com.p3k.magictale.engine.graphics.ResourceManager;

import java.util.ArrayList;

/**
 * Created by artem96 on 15.01.17.
 */
public class Bat extends Bot{

    public static final float BAT_WIDTH = 64;
    public static final float BAT_HEIGHT = 64;

    public static final String BAT_SPRITE_PATH = "res/animation/bat/bat.png";

    public Bat (float x, float y) {

        super(x, y, BAT_WIDTH, BAT_HEIGHT);

        // get player animations
        this.type = CharacterTypes.BAT_BOT;

        try {
            this.animations = (ArrayList<Animation>) ResourceManager.getInstance().getAnimations(this).clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setGainedExperience(500);

        setAttack(5);

        setCharacterSize(BAT_WIDTH*4, BAT_HEIGHT*4);

    }
}
