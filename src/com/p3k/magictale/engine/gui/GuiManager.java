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

        float offset = 70.0f;


        MButton character = factory.createButton("Character", 0, 50.0f);
        character.resize(200, 50);
        MButton inventory = factory.createButton("Inventory", 200f, 50.0f);
        inventory.resize(200, 50);
        MButton chat = factory.createButton("Chat", 400f, 50.0f);
        chat.resize(200, 50);
        MButton exit = factory.createButton("Exit", 600f, 50.0f);
        exit.resize(200, 50);

        objects.put("char", character);
        objects.put("inventory", inventory);
        objects.put("chat", chat);
        objects.put("exit", exit);
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
