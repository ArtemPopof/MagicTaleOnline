package com.p3k.magictale.engine.gui;

import client.Player;
import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by jorgen on 14.12.16.
 */
public abstract class ComponentFactory {

    protected static String RES_PATH = "";

    public abstract Button createButton(String text, float x, float y);
    public abstract Button createButtonSmall(Sprite image, float x, float y);

    public abstract Text createText(String text);
    public abstract Text createText(String text, String font);

    public abstract StatusBar createStatusBar(Player player);

    public abstract ActionBar createActionBar(Player player);

    public abstract PlayerMenu createPlayerMenu();

    public abstract Window createWindow(Sprite background, String title);

    public abstract Inventory createInventory(Player player);
}
