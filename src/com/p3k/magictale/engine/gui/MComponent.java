package com.p3k.magictale.engine.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgen on 06.12.16.
 */
public abstract class MComponent {
    protected static final int MIN_HEIGHT = 20;
    protected static final int MIN_WIDTH = 20;

    protected MComponent parent;
    protected List<MComponent> children;

    protected   float x, y;
    protected   int width, height;

    protected boolean isHovered, isPressed;

    protected boolean isResized;

    public MComponent(MComponent parent) {
        this.parent = parent;
        children = new ArrayList<>();

        this.width = MIN_WIDTH;
        this.height = MIN_HEIGHT;

        this.x = this.y = 0;

        this.isHovered = this.isPressed = false;
        this.isResized = false;

        // Default font for all components
        // TODO font
    }

    public abstract void render();

    public abstract void update();

    public MComponent move(float x, float y) {
        this.x = x;
        this.y = y;


        return this;
    }

    public MComponent resize(int width, int height) {

        this.width = width;
        this.height = height;

        isResized = true;
        onResized();
        return this;
    }

    public MComponent put(MComponent child) {
        children.add(child);
        child.setParent(this);

        this.update();

        return this;
    }

    public MComponent putAll(ArrayList<MComponent> children) {
        children.forEach(child -> {
            child.setParent(this);
            this.children.add(child);
        });

        this.update();

        return this;
    }

    public MComponent getParent() {
        return parent;
    }

    public MComponent setParent(MComponent parent) {
        this.parent = parent;
        return this;
    }

    public MComponent removeChild(MComponent child) {
        children.remove(child);

        update();

        return this;
    }

    public void remove() {
        this.parent.removeChild(this);

        for (MComponent child : children) {
            child.setParent(null);
        }
    }

    /**
     * Returns true if given point belongs to component
     * @param x x coordinate (OpenGL style)
     * @param y y coordinate (OpenGL style)
     * @return true if point (x, y) belongs component
     *
     * @apiNote x, y = 0 if point in left bottom corner of the screen
     */
    public boolean isPointBelongs(int x, int y) {
        return x >= this.x && x <= this.x + this.width &&
               y <= this.y && y >= this.y - this.height;
    }

    // EVENTS

    protected abstract void onResized();

    // MOUSE

    public abstract void onMouseOver();

    public abstract void onMouseOut();

    public abstract void onMouseMove();

    public abstract void onMousePressed();

    public abstract void onMouseReleased();

    // GET/SET

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.move(x, y);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.move(x, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        resize(width, this.height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        resize(this.width, height);
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}
