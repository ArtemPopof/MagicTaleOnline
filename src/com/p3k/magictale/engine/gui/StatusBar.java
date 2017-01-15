package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.Logger;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.game.Characters.Player;
import org.lwjgl.opengl.GL11;

/**
 * Created by jorgen on 20.12.16.
 */
public class StatusBar extends Widget {

    Player player;
    Widget hpBar;

    /**
     * Creates player statusbar
     * @param sprite Status bar sprite
     * @param player Player to get status
     */
    public StatusBar(Sprite sprite, Player player) {
        super(null, sprite);

        this.player = player;
        this.hpBar = new Widget(this, new Sprite(0.8f, 0.3f, 0.3f,
                    this.width,
                    this.height * 0.29f));

        float paddingLeft =  (this.width * 0.545f); // 0.8f - bar width (full hp)
        float paddingTop  =  (-this.height * 0.14f); // 0.9f - bar height
        hpBar.move(paddingLeft, paddingTop);

        this.put(hpBar);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render() {
        super.render();

        // Render HP bar

        float percent =
                this.player.getCurrentHealth() / (float) this.player.getMaxHealth();

        Logger.log("Percent: " + percent, Logger.DEBUG);

        int barWidth = (int) (this.width * 0.53f * percent);
        barWidth = Math.max(barWidth, 4);

        this.hpBar.setWidth(barWidth);

        // TODO: Render exp bar

        // TODO: Render player icon
    }
}
