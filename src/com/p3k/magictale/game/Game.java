package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;

import java.util.ArrayList;

/**
 * Game routines
 * Created by artem96 on 03.12.16.
 */
public class Game {
    private static Game instance = null;

    private ArrayList<GameObject> objects;
    private Player player;
    private Level levelManager;

    private ResourceManager resourceManager;

    private SoundManager soundManager;
    private SoundSource bgmSound;
    private SoundSource envSound;

    private float cameraX = 0;
    private float cameraY = 0;


    private final String mapName = "forest";

    private Game() {

        try {
            resourceManager = ResourceManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            levelManager = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }
        try {
            levelManager.load(mapName, resourceManager);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }

        initSoundManager();



        initObjects();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }


    public void processInput() {
        player.processInput();
    }

    public void update() {

//        levelManager.update();

        for (GameObject object : objects) {
            object.update();
        }

    }

    public void render() {

        levelManager.render();

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
        bgmSound.setLevel(0.8f).play("main_theme.wav");
        envSound.setLevel(0.8f).play("wind.wav");
    }

    public Level getLevelManager() {
        return levelManager;
    }

    public float getCameraX() {
        return cameraX;
    }

    public void setCameraX(float cameraX) {
        this.cameraX = cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    public void moveCamera(float x, float y) {
        this.cameraX += x;
        this.cameraY += y;
    }
}
