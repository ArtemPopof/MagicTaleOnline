package com.p3k.magictale.engine.graphics;

/**
 * Created by artem96 on 03.12.16.
 */

import static org.lwjgl.opengl.GL11.*;

public class GameObject {
    protected float x;
    protected float y;

    protected Sprite sprite;

    public void render() {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            glTranslatef(x, y, 0);

            sprite.render();

        }
        glPopMatrix();
    }

    public void update() {

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


    public float getWidth() {
        return sprite.getWidth();
    }

    public void setWidth(float width) {
        sprite.setWidth(width);
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setHeight(float height) {
        sprite.setHeight(height);
    }

    protected void init(float x, float y, float r, float g, float b, float width, float height) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(r, g, b, width, height);
    }

}
