package com.p3k.magictale.graphics;

import javafx.animation.Animation;

/**
 * Created by artem96 on 03.12.16.
 */
public class GameObject {
    private float x;

    private float y;
    private Animation animation;


    public void render() {

    }

    public void update() {

    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


}
