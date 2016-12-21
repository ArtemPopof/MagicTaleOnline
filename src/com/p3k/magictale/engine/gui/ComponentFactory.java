package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;

/**
 * Created by jorgen on 14.12.16.
 */
public abstract class ComponentFactory {

    protected String RES_PATH = "";

    public abstract Button createButton(String text, float x, float y);

    public abstract Button createButtonSmall(Sprite image, float x, float y);

    public abstract Text createText(String text);

    public abstract StatusBar createStatusBar(Player player);

    public abstract ActionBar createActionBar(Player player);

    public abstract PlayerMenu createPlayerMenu();
}
