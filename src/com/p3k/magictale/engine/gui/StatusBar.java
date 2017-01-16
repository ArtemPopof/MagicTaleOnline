package com.p3k.magictale.engine.gui;

import client.Player;
import com.p3k.magictale.engine.Logger;
import com.p3k.magictale.engine.graphics.Sprite;
import common.remoteInterfaces.GameController;

import java.rmi.RemoteException;

/**
 * Created by jorgen on 20.12.16.
 */
public class StatusBar extends Widget {

    Player player;
    Widget playerIcon;
    Widget hpBar;
    Widget xpBar;
    Text playerLevel;

    /**
     * Creates player statusbar
     * @param sprite Status bar sprite
     * @param player Player to get status
     */
    public StatusBar(Sprite sprite, Player player) {
        super(null, sprite);

        this.player = player;

        // XP and HP bars
        this.hpBar = new Widget(this, new Sprite(0.8f, 0.3f, 0.3f,
                    this.width,
                    this.height * 0.29f));
        this.xpBar = new Widget(this, new Sprite(0.3f, 0.8f, 0.3f,
                    this.width,
                    this.height * 0.29f));

        float paddingLeft =  (this.width * 0.545f); // 0.8f - bar width (full hp)
        float paddingTop  =  (-this.height * 0.14f); // 0.9f - bar height
        hpBar.move(paddingLeft, paddingTop);

        paddingTop = (-this.height * 1.05f);
        xpBar.move(paddingLeft, paddingTop);

        // PLAYER ICON
        this.playerIcon = new Widget(this, this.player.getSprite());

        paddingLeft = 6.0f;
        paddingTop =  -5.0f;
        playerIcon.move(paddingLeft, paddingTop);

        ComponentFactory factory = new StdComponentFactory();

        // PLAYER LEVEL TEXT
        this.playerLevel = factory.createText("Level: ");
        this.playerLevel.move(this.x + 7.0f, -this.height - 12.0f);


        this.put(hpBar);
        this.put(xpBar);
        this.put(playerLevel);
        this.put(playerIcon);
    }

    @Override
    public void update()  {
        super.update();

        float percent;
        percent = this.player.getCurrentHealth() / (float) this.player.getMaxHealth();

        int barWidth = (int) (this.width * 0.52f * percent);
        barWidth = Math.max(barWidth, 2);

        this.hpBar.setWidth(barWidth);

        percent = this.player.getXp() / (float) this.player.getXpForNextLevel();

        barWidth = (int) (this.width * 0.52f * percent);
        barWidth = Math.max(barWidth, 2);

        this.xpBar.setWidth(barWidth);

        this.playerIcon.setBackground(this.player.getSprite());
        this.playerIcon.resize(
                (int) (this.width * 0.25f),
                (int) (this.width * 0.25f));

        this.playerLevel.setText("Level: " + player.getCurrentLevel());
    }

    @Override
    public void render() {
        super.render();
    }
}
