package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.Logger;
import com.p3k.magictale.engine.graphics.Sprite;
import common.remoteInterfaces.GameController;

import java.rmi.RemoteException;

/**
 * Created by jorgen on 20.12.16.
 */
public class StatusBar extends Widget {

    GameController player;
    Widget hpBar;
    Widget playerIcon;

    /**
     * Creates player statusbar
     * @param sprite Status bar sprite
     * @param player Player to get status
     */
    public StatusBar(Sprite sprite, GameController player) {
        super(null, sprite);

        this.player = player;
        this.hpBar = new Widget(this, new Sprite(0.8f, 0.3f, 0.3f,
                    this.width,
                    this.height * 0.29f));

        float paddingLeft =  (this.width * 0.545f); // 0.8f - bar width (full hp)
        float paddingTop  =  (-this.height * 0.14f); // 0.9f - bar height
        hpBar.move(paddingLeft, paddingTop);

        //this.playerIcon = new Widget(this, this.player.getSprite());

        paddingLeft = (this.width * 0.08f);
        paddingTop = (-this.height * 0.08f);
        //playerIcon.move(paddingLeft, paddingTop);

        //this.put(playerIcon);
        this.put(hpBar);
    }

    @Override
    public void update()  {
        super.update();

        float percent;
        try {
            percent = this.player.getHealth() / (float) this.player.getMaxHealth();

            Logger.log("HP BAR WIDTH Percent: " + percent, Logger.DEBUG);

            int barWidth = (int) (this.width * 0.78f * percent);
            barWidth = Math.max(barWidth, 4);

            this.hpBar.setWidth(barWidth);

            //this.playerIcon.setBackground(this.player.getSprite());
            //this.playerIcon.setWidth((int) (playerIcon.getWidth() * 0.60f));
            //this.playerIcon.setHeight((int) (playerIcon.getHeight() * 0.60f));
        } catch (RemoteException e) {
            Logger.log("Status bar update failed. Cannot get player info.", Logger.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        super.render();
    }
}
