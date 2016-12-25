package com.p3k.magictale.engine.gui;

import client.ClientGame;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Characters.Player;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by jorgen on 13.12.16.
 */
public class GuiManager extends MComponent implements Constants {

    private ComponentFactory factory;
    private Map<String, MComponent> objects;

    private Player player; // An user of all this stuff

    public GuiManager(Player player) {
        super(null);

        this.player = player;

        this.objects = new HashMap<>();
        this.factory = new StdComponentFactory();

        createHud();
    }

    /**
     * Creates all top level
     * widgets (HUD)
     */
    private void createHud() {

        // Status bar
        StatusBar statusBar = this.factory.createStatusBar(this.player);
        statusBar.resize((int) (statusBar.getWidth() * 1.5f), (int) (statusBar.getHeight() * 1.5f));
        statusBar.move(8, WINDOW_HEIGHT - 8);

        // Action bar
        ActionBar actionBar = this.factory.createActionBar(this.player);
        actionBar.resize((int) (actionBar.getWidth() * 1.5f), (int) (actionBar.getHeight() * 1.5f));
        actionBar.move(((WINDOW_WIDTH - actionBar.getWidth()) / 2), actionBar.getHeight() + 5);

        // Player menu
        PlayerMenu playerMenu = this.factory.createPlayerMenu();
        playerMenu.setHeight((int) (playerMenu.height * 1.5));
        playerMenu.move(WINDOW_WIDTH - playerMenu.getWidth(), playerMenu.getHeight());

        // Inventory

        this.put("statusBar", statusBar);
        this.put("actionBar", actionBar);
        this.put("playerMenu", playerMenu);
    }

    @Override
    public void render() {

        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.objects.keySet().forEach(name -> {
            this.objects.get(name).render();
        });
    }

    @Override
    public void update() {

        // MOUSE MOVE
        if (((ClientGame) ClientGame.getInstance()).isMouseMoved()) {
            for (MComponent child : this.children) {

                if ( child.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {

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
            }
        }

        // MOUSE PRESSED
        if (((ClientGame) ClientGame.getInstance()).isMousePressed()) {
            for (MComponent child : this.children) {

                if ( child.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {
                    child.setPressed(true);
                    child.onMousePressed();
                }
            }
        } else if (((ClientGame) ClientGame.getInstance()).isMouseReleased()) {
            for (MComponent child : this.children) {

                if ( child.isPointBelongs(Mouse.getX(), Mouse.getY()) ) {
                    child.setPressed(false);
                    child.onMouseReleased();
                }
            }
        }

    }

    public MComponent put(String name, MComponent child) {
        this.objects.put(name, child);
        this.children.add(child);
        child.setParent(this);

        return this;
    }

    @Override
    protected void onResized() {

    }

    @Override
    public void onMouseOver() {

    }

    @Override
    public void onMouseOut() {

    }

    @Override
    public void onMouseMove() {

    }

    @Override
    public void onMousePressed() {

    }

    @Override
    public void onMouseReleased() {

    }

    public void add(String name, MComponent object) {
        this.objects.put(name, object);
    }

    public void remove(MComponent object) {
        this.objects.remove(object);
    }

    public MComponent get(String name) {
        return this.objects.get(name);
    }
}
