package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
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

    private SoundManager soundManager;
    private SoundSource bgmSound;
    private SoundSource envSound;

    public Game() {

//        level = LevelLoad.load();

        objects = new ArrayList<>();

        player = new Player(250, 250);

        objects.add(player);

        try {
            soundManager = SoundManager.getInstance();

            soundManager.registerSound("main_theme.wav");
            soundManager.registerSound("wind.wav");
        } catch (Exception e) {
            System.err.println("Error initializing sound manager: " + e);
        }


        try {
            bgmSound = new SoundSource(null, true);
            envSound = new SoundSource(null, true);

        } catch (Exception e) {
            System.err.println("Error loading bgm sounds: " + e);
        }

        // Must be moved to more appropriate place?
        bgmSound.setLevel(0.15f).play("main_theme.wav");
        envSound.setLevel(1.0f).play("wind.wav");

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

    public void cleanUp() {

    }
}
