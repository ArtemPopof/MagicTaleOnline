package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by jorgen on 20.12.16.
 */

/**
 * Element that must have a background sprite.
 * Can be rendered. Can be moved optionally.
 */
public class Widget extends MComponent {

    protected Sprite background;

    public Widget(MComponent parent, Sprite background) {
        super(parent);

        this.background = background;
        this.resize((int) background.getWidth(),
                    (int) background.getHeight());
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslatef(this.x, this.y, 0);
        background.render();

        children.forEach(child -> {
            child.render();
        });

        glPopMatrix();
    }

    @Override
    public void update() {
        children.forEach(child -> {
            child.update();
        });
    }

    @Override
    protected void onResized() {
        this.background.setWidth(width);
        this.background.setHeight(height);
    }

    @Override
    public void onMouseOver() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {
                child.setHovered(true);
                onMouseOver();
            }
        });
    }

    @Override
    public void onMouseOut() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {
                child.setHovered(false);
                child.setPressed(false);
                child.onMouseOut();
            }
        });
    }

    @Override
    public void onMouseMove() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {
                child.onMouseMove();
            } else {
                if ( child.isHovered() || child.isPressed() ) {
                    child.setHovered(false);
                    child.setPressed(false);
                    child.onMouseOut();
                }
            }
        });
    }

    @Override
    public void onMousePressed() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {
                child.setPressed(true);
                child.onMousePressed();
            }
        });
    }

    @Override
    public void onMouseReleased() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {
                child.setPressed(false);
                child.onMouseReleased();
            }
        });
    }

    public Sprite getBackground() {
        return background;
    }

    public void setBackground(Sprite background) {
        this.background = background;
    }
}
