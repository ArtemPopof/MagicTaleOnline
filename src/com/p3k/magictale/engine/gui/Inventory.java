package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;
import common.remoteInterfaces.GameController;

/**
 * Created by jorgen on 24.12.16.
 */
public class Inventory extends Window {
    private GameController player;

    public Inventory(Sprite sprite, Text title, GameController player) {
        super(sprite, title, null);

        this.player = player;
    }
}
