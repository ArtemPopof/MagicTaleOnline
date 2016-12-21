package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;

/**
 * Created by jorgen on 20.12.16.
 */
public class StatusBar extends Widget {

    Player player;

    /**
     * Creates player statusbar
     * @param sprite Status bar sprite
     * @param player Player to get status
     */
    public StatusBar(Sprite sprite, Player player) {
        super(null, sprite);

        this.player = player;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render() {
        super.render();

        // TODO: Render hp and sp bars

        // TODO: Render player icon
    }
}
