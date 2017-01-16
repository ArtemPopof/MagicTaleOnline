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

        if ( this.background != null ) {
            this.resize((int) background.getWidth(),
                    (int) background.getHeight());
        }
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslatef(this.x, this.y, 0);
        if ( background != null) {
            background.render();
        }

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
        if ( background != null ) {
            this.background.setWidth(width);
            this.background.setHeight(height);
        }
    }

    @Override
    public void onMouseOver() {
    }

    @Override
    public void onMouseOut() {
    }

    @Override
    public void onMouseMove() {
        children.forEach(child -> {
            if ( child.isPointBelongs(
                    (int) (Mouse.getX() - this.x),
                    (int) (Mouse.getY() - this.y)) ) {

                // That element has already been over'ed
                if ( child.isHovered() ) {
                    child.onMouseMove();
                } else {
                    child.setHovered(true);
                    child.onMouseOver();
                }
            } else if ( child.isHovered() ) { // Mouse not on object yet
                child.setHovered(false);
                child.onMouseOut();
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
