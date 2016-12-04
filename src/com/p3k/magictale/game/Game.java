package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.game.objects.Player;
import com.p3k.magictale.map.level.Level;

import java.util.ArrayList;

/**
 * Game routines
 * Created by artem96 on 03.12.16.
 */
public class Game {

    private ArrayList<GameObject> objects;
    private Player player;
    private Level level;

    public Game() {

//        level = LevelLoad.load();

        objects = new ArrayList<>();

        player = new Player(250, 250);

        objects.add(player);

    }


    public void processInput() {
        player.processInput();
    }

    public void update() {

//        level.update();

        for (GameObject object : objects) {
            object.update();
        }

    }

    public void render() {

//        level.render();

        for (GameObject object : objects) {
            object.render();
        }

    }
}
