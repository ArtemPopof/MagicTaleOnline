package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;
import common.remoteInterfaces.GameController;

/**
 * Created by jorgen on 20.12.16.
 */
public class ActionBar extends Widget {

    private GameController player;
    private boolean fixed;

    public ActionBar(Sprite background, GameController player) {
        super(null, background);

        this.player = player;
        this.fixed = true;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render() {
        super.render();

        // TODO: Render player skills

    }

    public boolean isFixed() {
        return fixed;
    }

    public void setPlayer(GameController player) {
        this.player = player;
    }
}
