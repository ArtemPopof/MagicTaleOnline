package com.p3k.magictale.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by artem96 on 03.12.16.
 */
public class Sprite {

    private float r, g, b;

    private float width;
    private float height;


    public Sprite(float r, float g, float b, float width, float height) {
        this.r = r;
        this.g = g;
        this.b = b;

        this.width = width;
        this.height = height;
    }

    public void render() {

        glColor3f(r, g, b);

        glBegin(GL_QUADS); {

            glVertex2f(0, 0);
            glVertex2f(0, height);
            glVertex2f(width, height);
            glVertex2f(width, 0);

        }
        glEnd();
    }


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
