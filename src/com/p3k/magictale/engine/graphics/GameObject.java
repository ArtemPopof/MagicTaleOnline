package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.game.Game;
import com.p3k.magictale.engine.enums.Direction;

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
     */
    protected Direction direction;
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

    /**
     * return right side of sprite
     *
     * @return
     */
    public float getRealX() {
        return x;
    }

    /**
     * return bottom side of sprite
     *
     * @return
     */
    public float getRealY() {
        return y - getHeight()/2;
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

        direction = Direction.DOWN;
    }

    protected void initBySprite(Sprite sprite, float x, float y) {
        this.sprite = sprite;
//        this.sprite = new Sprite(sprite.getTextureId(), 32, 32);
        this.x = x;
        this.y = y;

        direction = Direction.DOWN;
    }

    /**
     * set direction of sprite
     * @param direction - direction to be set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * get current object direction
     * @return direction of object
     *
     */
    public Direction getDirection() {
        return direction;
    }

}
