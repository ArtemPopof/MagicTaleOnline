package com.p3k.magictale.engine.gui;

/**
 * Created by jorgen on 14.12.16.
 */

import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.SpriteSheet;
import com.p3k.magictale.engine.gui.fonts.Font;
import com.p3k.magictale.game.Characters.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates standard look gui elements
 */
public class StdComponentFactory extends ComponentFactory {

    // Button images texture names
    Sprite btnNormal, btnHovered, btnPressed;

    // Small buttons spritesheet
    SpriteSheet btnSmall;
    int btnSmallWidth = 32;
    int btnSmallHeight = 16;

    // Std font
    Font defaultFont;

    // Std ui sprites
    Sprite statusBar, actionBar;

    // Inventory
    Sprite inventory;

    // PlayerMenu
    Sprite container;
    Sprite characterButton, inventoryButton, mapButton, mainMenuButton;

    public StdComponentFactory() {
        RES_PATH = "res/gui/std";

        try {
            // BUTTONS
            BufferedImage buffer;

            buffer = Utils.loadImage(RES_PATH + "/button_normal.bmp");
            btnNormal = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());
            buffer = Utils.loadImage(RES_PATH + "/button_hovered.bmp");
            btnHovered = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());
            buffer = Utils.loadImage(RES_PATH + "/button_pressed.bmp");
            btnPressed = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

            btnSmall = new SpriteSheet(RES_PATH + "/buttons_small.png",
                    btnSmallWidth, btnSmallHeight);

            // FONT
            defaultFont = new Font("pixel_regular");

            // STATUS BAR
            buffer = Utils.loadImage(RES_PATH + "/status_bar.png");
            statusBar = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

            // ACTION BAR
            buffer = Utils.loadImage(RES_PATH + "/action_bar.png");
            actionBar = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

            // INVENTORY
            buffer = Utils.loadImage(RES_PATH + "/inventory.png");
            inventory = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

            // PLAYER MENU
            buffer = Utils.loadImage(RES_PATH + "/container.png");
            container = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

            buffer = Utils.loadImage(RES_PATH + "/character_button.png");
            characterButton = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());
            buffer = Utils.loadImage(RES_PATH + "/inventory_button.png");
            inventoryButton = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());
            buffer = Utils.loadImage(RES_PATH + "/map_button.png");
            mapButton = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());
            buffer = Utils.loadImage(RES_PATH + "/main_menu_button.png");
            mainMenuButton = new Sprite(buffer, buffer.getWidth(), buffer.getHeight());

        } catch (IOException e) {
            System.err.println("Creating StdComponentFactory error: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Creating StdComponentFactory error: " + e);
            e.printStackTrace();
        }
    }

    public Text createText(String text) {
        return new Text(text, defaultFont);
    }

    @Override
    public StatusBar createStatusBar(Player player) {
        return new StatusBar(
                new Sprite(statusBar.getTextureId(),
                        statusBar.getWidth(),
                        statusBar.getHeight()),
                player);
    }

    @Override
    public ActionBar createActionBar(Player player) {
        return new ActionBar(
                new Sprite(actionBar.getTextureId(),
                        actionBar.getWidth(),
                        actionBar.getHeight()),
                player);
    }

    @Override
    public PlayerMenu createPlayerMenu() {
        PlayerMenu menu = new PlayerMenu(container);

        ArrayList<MComponent> buttons = new ArrayList<>();

        int width =  (int) characterButton.getWidth();
        int height = (int) characterButton.getHeight();

        Button character = this.createButtonSmall(characterButton, 0, 0);
        character.resize(24, 18);
        Button inventory = this.createButtonSmall(inventoryButton, 0, 0);
        inventory.resize(24, 18);
        Button map       = this.createButtonSmall(mapButton, 0, 0);
        map.resize(24, 18);
        Button mainMenu  = this.createButtonSmall(mainMenuButton, 0, 0);
        mainMenu.resize(24, 18);

        buttons.add(character);
        buttons.add(inventory);
        buttons.add(map);
        buttons.add(mainMenu);

        menu.putAll(buttons);

        return menu;
    }

    //@Override
    //public  ActionBar createActionBar(Player player) {

    //}

    @Override
    public Button createButton(String text, float x, float y) {
        Button button = new Button(createText(text), x, y);

        button.setNormalSprite(new Sprite(btnNormal.getTextureId(),
                btnNormal.getWidth(), btnNormal.getHeight()));
        button.setHoveredSprite(new Sprite(btnHovered.getTextureId(),
                btnHovered.getWidth(), btnHovered.getHeight()));
        button.setPressedSprite(new Sprite(btnPressed.getTextureId(),
                btnPressed.getWidth(), btnPressed.getHeight()));

        // Need to adjust button and text sizes
        button.update();

        return button;
    }

    @Override
    public Button createButtonSmall(Sprite image, float x, float y) {
        Button button = new Button(image, x, y);

        ArrayList<Sprite> sprites = btnSmall.getSprites();

        button.setNormalSprite(new Sprite(sprites.get(0).getTextureId(),
                btnSmallWidth, btnSmallHeight));
        button.setHoveredSprite(new Sprite(sprites.get(1).getTextureId(),
                btnSmallWidth, btnSmallHeight));
        button.setPressedSprite(new Sprite(sprites.get(2).getTextureId(),
                btnSmallWidth, btnSmallHeight));

        button.resize(btnSmallWidth, btnSmallHeight);

        return button;
    }
}
