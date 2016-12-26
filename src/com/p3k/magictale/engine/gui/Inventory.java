package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;

/**
 * Created by jorgen on 24.12.16.
 */
public class Inventory extends Window {
    private Player player;

    public Inventory(Sprite sprite, Text title, Player player) {
        super(sprite, title, null);

        this.player = player;
    }
}
