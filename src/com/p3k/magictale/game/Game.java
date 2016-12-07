package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.objects.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;

import java.util.ArrayList;

/**
 * Game routines
 * Created by artem96 on 03.12.16.
 */
public class Game {

    private ArrayList<GameObject> objects;
    private Player player;
    private Level level;

    private ResourceManager resourceManager;

    private SoundManager soundManager;
    private SoundSource bgmSound;
    private SoundSource envSound;

    private final String mapName = "forest";

    public Game() {

        try {
            resourceManager = ResourceManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            level = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing level manager: " + e);
        }
        try {
            level.load(mapName, resourceManager);
        } catch (Exception e) {
            System.err.println("Error render level manager: " + e);
        }
        try {
            level.render();
        } catch (Exception e) {
            System.err.println("Error render level manager: " + e);
        }



        initSoundManager();



        initObjects();
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

        level.render();

        for (GameObject object : objects) {
            object.render();
        }

    }

    public void cleanUp() {

    }

    private void initObjects() {
        objects = new ArrayList<>();

        player = new Player(100 , 520);

        objects.add(player);

        // test character
        GameCharacter testChar = new GameCharacter(300, 550, 32, 64);
        objects.add(testChar);

    }

    private void initSoundManager() {
        try {
            soundManager = SoundManager.getInstance();

            soundManager.registerSound("main_theme.wav");
            soundManager.registerSound("wind.wav");
            soundManager.registerSound("user/baphomet_breath.wav");
            soundManager.registerSound("user/attack_axe.wav");
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
        //bgmSound.setLevel(0.5f).play("main_theme.wav");
        envSound.setLevel(1.0f).play("wind.wav");
    }
}
