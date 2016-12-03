package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.game.objects.Player;

import java.util.ArrayList;

/**
 * Game routines
 * Created by artem96 on 03.12.16.
 */
public class Game {

    private ArrayList<GameObject> objects;
    private Player player;

    public Game() {

        objects = new ArrayList<>();

        player = new Player(50, 50);

        objects.add(player);

    }


    public void processInput() {
        player.processInput();
    }

    public void update() {

        for (GameObject object : objects) {
            object.update();
        }

    }

    public void render() {

        for (GameObject object : objects) {
            object.render();
        }

    }
}
