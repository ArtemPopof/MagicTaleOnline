package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.game.Game;

import static org.lwjgl.opengl.GL11.*;

/**
 * GameObject
 *
 * Repreresnts some dynamic object with
 * animations, states, etc
 *
 * NPC, Player, or Bullet class should
 * be derived from this one.
 *
 * Created by artem96 on 03.12.16.
 */
public class GameObject {
    protected float x;
    protected float y;

    /**
     * Object current orientation
     * in space
     *
     * in degrees
     *
     */
    protected float direction;
    /**
     * Visual information of this object
     */
    protected Sprite sprite;

    public void render() {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            float cameraX = Game.getInstance().getCameraX();
            float cameraY = Game.getInstance().getCameraY();

            glTranslatef(x - cameraX, y - cameraY, 0);

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

    protected void init(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(0, 1f, 0, width, height);

        direction = 0;
    }

    protected void initBySprite(Sprite sprite, float x, float y) {
        this.sprite = sprite;
//        this.sprite = new Sprite(sprite.getTextureId(), 32, 32);
        this.x = x;
        this.y = y;

        direction = 0;
    }

}
