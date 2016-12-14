package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Game;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by jorgen on 13.12.16.
 */
public class GuiManager implements Constants {
    ComponentFactory factory;
    Map<String, MComponent> objects;

    public GuiManager() {
        objects = new HashMap<>();
        factory = new StdComponentFactory();

        MButton hello = factory.createButton("Hello, button!", 50.0f, 140.0f);
        hello.resize(54, 18);
        MButton goodbye = factory.createButton("Goodbye, button!", 50.0f, 100.0f);
        goodbye.resize(54, 18);

        objects.put("hello", hello);
        objects.put("goodbye", goodbye);
    }

    public void render() {

        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        objects.keySet().forEach( name -> {
            objects.get(name).render();
        });
    }

    public void update() {

        MComponent obj;

        // MOUSE MOVE
        if ( Game.getInstance().isMouseMoved() ) {
            for ( String key : objects.keySet() ) {
                obj = objects.get(key);

                if ( obj.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {

                    // That element has already been over'ed
                    if ( obj.isHovered() ) {
                        obj.onMouseMove();
                    } else {
                        obj.setHovered(true);
                        obj.onMouseOver();
                    }
                } else if ( obj.isHovered() ) { // Mouse not on object yet
                    obj.setHovered(false);
                    obj.onMouseOut();
                }
            }
        }

        // MOUSE PRESSED
        if ( Game.getInstance().isMousePressed() ) {
            for ( String key : objects.keySet() ) {

                obj = objects.get(key);

                if ( obj.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {
                    obj.setPressed(true);
                    obj.onMousePressed();
                }
            }
        } else if ( Game.getInstance().isMouseReleased() ) {
            for ( String key : objects.keySet() ) {

                obj = objects.get(key);

                if ( obj.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {
                    obj.setPressed(false);
                    obj.onMouseReleased();
                }
            }
        }

    }

    public void add(String name, MComponent object) {
        objects.put(name, object);
    }

    public void remove(MComponent object) {
        objects.remove(object);
    }

    public MComponent get(String name) {
        return objects.get(name);
    }
}
