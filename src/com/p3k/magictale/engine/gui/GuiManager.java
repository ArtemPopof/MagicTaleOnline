package com.p3k.magictale.engine.gui;

import client.ClientGame;
import client.Player;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.Logger;
import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.engine.graphics.Sprite;
import org.lwjgl.input.Mouse;
import server.ServerGame;
import sun.rmi.runtime.Log;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by jorgen on 13.12.16.
 */
public class GuiManager extends MComponent implements Constants {

    private ComponentFactory factory;
    private Map<String, MComponent> objects;

    private Player playerController; // An user of all this stuff

    private Text gameOverText;
    private Text mousePosition;
    private Text fps;

    private Widget cursor;

    private boolean softwareCursor = false;

    public GuiManager(Player player) {
        super(null);

        Logger.log("GuiManager here!", Logger.DEBUG);

        this.playerController = player;

        this.objects = new HashMap<>();
        this.factory = new StdComponentFactory();

        this.mousePosition = factory.createText("Mouser position: ", "regular");
        this.fps = factory.createText("FPS: ");

        this.add("mousePosition", mousePosition);
        this.add("fpsCounter", fps);

        this.mousePosition.setSize(16);

        this.gameOverText = factory.createText("GAME OVER. BUT YOU ARE AWESOME!", "big");
        this.gameOverText.move(
                WINDOW_WIDTH / 2 - (this.gameOverText.getWidth() / 2),
                WINDOW_HEIGHT / 2 + (this.gameOverText.getHeight() / 2));
        this.gameOverText.hide();

        this.mousePosition = factory.createText("Mouser position: ", "regular");
        this.fps = factory.createText("FPS: ");

        this.add("gameOverText", gameOverText);
        this.add("mousePosition", mousePosition);
        this.add("fpsCounter", fps);

        this.mousePosition.setSize(16);

        this.cursor = null;

        if (System.getProperty("os.name").startsWith("Windows")) {
            softwareCursor = true;
        }

        createHud();
    }

    /**
     * Creates all top level
     * widgets (HUD)
     */
    private void createHud() {

        // Status bar
        StatusBar statusBar = this.factory.createStatusBar(this.playerController);
        statusBar.resize((int) (statusBar.getWidth() * 1.5f), (int) (statusBar.getHeight() * 1.5f));
        statusBar.move(8, WINDOW_HEIGHT - 8);

        // Action bar
        ActionBar actionBar = this.factory.createActionBar(this.playerController);
        actionBar.resize((int) (actionBar.getWidth() * 1.5f), (int) (actionBar.getHeight() * 1.5f));
        actionBar.move(((WINDOW_WIDTH - actionBar.getWidth()) / 2), actionBar.getHeight() + 5);

        // Inventory
        Inventory inventory = factory.createInventory(playerController);
        inventory.move(WINDOW_WIDTH - inventory.getWidth() - 60, WINDOW_HEIGHT / 2 + inventory.getHeight());
        inventory.resize((int) (inventory.getWidth() * 1.5f),
                (int) (inventory.getHeight() * 1.5f));
        inventory.hide();

        // Player menu
        PlayerMenu playerMenu = this.factory.createPlayerMenu();
        playerMenu.setHeight((int) (playerMenu.height * 1.5));
        playerMenu.move(WINDOW_WIDTH - playerMenu.getWidth(), playerMenu.getHeight());
        playerMenu.setButtonAction(1, () -> {
            if (inventory.isShown()) {
                inventory.hide();
            } else {
                inventory.show();
            }
        });

        playerMenu.setButtonAction(0, () -> {
            try {
                ClientGame clientGame = (ClientGame) ClientGame.getInstance();
                clientGame.getController().placeRandomBots();
            } catch (RemoteException e) {
                Logger.log(e.getMessage(), Logger.ERROR);
            }
        });

        try {
            BufferedImage img = null;

            try {
                img = Utils.loadImage("res/cursor.png");
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

            this.cursor = new Widget(this, new Sprite(img, img.getWidth(), img.getHeight()));
        } catch (IOException e) {
            Logger.log("Error loading cursor: " + e.getMessage(), Logger.ERROR);
            e.printStackTrace();
        }

        this.put("statusBar", statusBar);
        this.put("actionBar", actionBar);
        this.put("playerMenu", playerMenu);
        this.put("inventory", inventory);
    }

    @Override
    public void render() {

        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        objects.keySet().forEach(name -> {
            if (objects.get(name).isShown()) {
                objects.get(name).render();
            }
        });

        if (ClientGame.isDebug()) {

            mousePosition.setText(
                    String.format("Mouse x = %d, y = %d", Mouse.getX(), Mouse.getY()));
            mousePosition.move(10, mousePosition.getHeight() + 10);
        }

        if (softwareCursor) {
            this.cursor.render();
        }
    }

    @Override
    public void update() {

        // MOUSE MOVE
        if (((ClientGame) ClientGame.getInstance()).isMouseMoved()) {
            for (MComponent child : this.children) {

                if (child.isPointBelongs(Mouse.getX(), Mouse.getY())) {

                    // That element has already been over'ed
                    if (child.isHovered()) {
                        child.onMouseMove();
                    } else {
                        child.setHovered(true);
                        child.onMouseOver();
                    }
                } else if (child.isHovered()) { // Mouse not on object yet
                    child.setHovered(false);
                    child.onMouseOut();
                }
            }
        }

        // MOUSE PRESSED
        if (((ClientGame) ClientGame.getInstance()).isMousePressed()) {
            for (MComponent child : this.children) {

                if (child.isPointBelongs(Mouse.getX(), Mouse.getY())) {
                    child.setPressed(true);
                    child.onMousePressed();
                }
            }
        } else if (((ClientGame) ClientGame.getInstance()).isMouseReleased()) {
            for (MComponent child : this.children) {

                if (child.isPointBelongs(Mouse.getX(), Mouse.getY())) {
                    child.setPressed(false);
                    child.onMouseReleased();
                }
            }
        }

        this.objects.get("statusBar").update();

        if (this.playerController.isDead()) {
            this.gameOverText.show();
        }

        if (this.cursor != null) {
            this.cursor.move(Mouse.getX(), Mouse.getY());
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
