package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.enums.Direction;
import com.p3k.magictale.game.Game;

import java.io.Serializable;

import static org.lwjgl.opengl.GL11.*;

/**
 * GameObject
 *
 * Some object in game
 * NPC, Player, or Bullet class should
 * be derived from this one.
 *
 * Created by artem96 on 03.12.16.
 */
public class GameObject implements Serializable {
    private static int maxId = 0;
    private final int id;
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

    // all values will be
    // specified later
    public GameObject() {
        this.id = maxId++;
    }

    public GameObject(float x, float y, float width, float height, int r, int g, int b) {
        this();
        this.init(x, y, width, height, r, g, b);
    }

    public GameObject(float x, float y, float width, float height) {
        this();
        init(x, y, width, height);
    }

    public GameObject(Sprite sprite, float width, float height) {
        this();
        initBySprite(sprite, width, height);
    }

    public void render() {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            float cameraX = Game.getInstance().getCameraX();
            float cameraY = Game.getInstance().getCameraY();

            glTranslatef(this.x - cameraX, this.y - cameraY, 0);

            this.sprite.render();

        }
        glPopMatrix();
    }

    public void update() {

    }

    public float getX() {
        return this.x;
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
        return this.x + getWidth() / 2;
    }

    /**
     * return bottom side of sprite
     *
     * @return
     */
    public float getRealY() {
        return this.y;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public float getWidth() {
        return this.sprite.getWidth();
    }

    public void setWidth(float width) {
        this.sprite.setWidth(width);
    }

    public float getHeight() {
        return this.sprite.getHeight();
    }

    public void setHeight(float height) {
        this.sprite.setHeight(height);
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    protected void setSprite(Sprite sprite) { this.sprite = sprite; }

    protected void init(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(0, 1f, 0, width, height);

        this.direction = Direction.DOWN;
    }

    protected void init(float x, float y, float width, float height, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(r, g, b, width, height);
    }

    protected void initBySprite(Sprite sprite, float x, float y) {
        this.sprite = sprite;
//        this.sprite = new Sprite(sprite.getTextureId(), 32, 32);
        this.x = x;
        this.y = y;

        this.direction = Direction.DOWN;
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
        return this.direction;
    }

    /**
     * Debug method. Just remove sprite from object
     */
    public void removeSprite() {
        this.sprite = new Sprite(1f, 0, 0, getWidth(), getHeight());
    }

    /**
     * @return id of any changeable object on map
     */
    public int getId() {
        return this.id;
    }
}
