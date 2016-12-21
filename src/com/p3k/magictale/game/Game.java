package com.p3k.magictale.game;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.gui.GuiManager;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.Characters.Bot;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import com.p3k.magictale.map.objects.ObjectInterface;
import com.p3k.magictale.map.objects.ObjectManager;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Game routines
 * Created by artem96 on 03.12.16.
 */
public class Game implements Constants {
    private static Game instance = null;

    private final String mapName = "forest_v2";
    private IGameObjects objects;

    private Player player;
    private Level levelManager;
    private ObjectInterface objectManager;
    private ResourceManager resourceManager;
    private SoundManager soundManager;
    private SoundSource bgmSound;
    private SoundSource envSound;
    private GuiManager guiManager;
    private float cameraX = 0;
    private float cameraY = 0;
    private boolean isMouseMoved = false;
    private boolean isMouseLeftPressed = false;
    private boolean isMouseRightPressed = false;
    private boolean isMouseLeftReleased = false;
    private boolean isMouseRightReleased = false;

    private Cursor cursor;

    private Game() {
        if (System.getenv("IP") == null) {
            try {
                LocateRegistry.createRegistry(1099);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        initLevelManager();

        // INITIALIZING CURSOR
        try {
            cursor = ResourceManager.getInstance().loadCursor("res/cursor.png");

            Mouse.setNativeCursor(cursor);
        } catch (Exception e) {
            System.err.println("Error loading cursor");
        }

        initSoundManager();

        initGuiManager();

        try {
            initObjects();
        } catch (RemoteException | AlreadyBoundException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        initObjectManager();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }


    public void processInput() {

        try {
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);

                if (GameCharacter.class.isInstance(object)) {
                    GameCharacter character = (GameCharacter) object;
                    character.processInput();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Mouse handle
        isMouseMoved = Mouse.getDX() != 0 || Mouse.getDY() != 0;
        isMouseLeftReleased = isMouseLeftPressed && !Mouse.isButtonDown(MOUSE_BTN_LEFT);
        isMouseRightReleased = isMouseRightPressed && !Mouse.isButtonDown(MOUSE_BTN_RIGHT);
        isMouseLeftPressed = Mouse.isButtonDown(MOUSE_BTN_LEFT);
        isMouseRightPressed = Mouse.isButtonDown(MOUSE_BTN_RIGHT);
    }

    public void update() {

//        levelManager.update();

        try {
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                object.update();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        guiManager.update();
    }

    public void render() {

        levelManager.render();

        objectManager.render(1);

        try {
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                object.render();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        objectManager.render(2);

        guiManager.render();
    }

    public void cleanUp() {

    }

    private void initObjects() throws RemoteException, AlreadyBoundException, MalformedURLException, NotBoundException {
        String remoteIP = System.getenv("IP");
        if (remoteIP == null) {
            objects = new GameObjects();
            Naming.bind("GameObjects", objects);
            System.out.println("RMI backend for objects created");
        } else {
            Registry registry = LocateRegistry.getRegistry(remoteIP);
            objects = (IGameObjects) registry.lookup("GameObjects");
            System.out.println("remote objects in use");
        }

        player = new Player(100, 520);

        objects.add(player);

        // test bot
        Bot testBot = new Bot(500, 400, 64, 64);
        objects.add(testBot);

        // test bot
        Bot testBot2 = new Bot(200, 354, 64, 64);
        objects.add(testBot2);

        // test bot
        Bot testBot3 = new Bot(252, 272, 64, 64);
        objects.add(testBot3);

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
        //  bgmSound.setLevel(0.8f).play("main_theme.wav");
        //  envSound.setLevel(0.8f).play("wind.wav");
    }

    private void initLevelManager() {
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
    }

    private void initObjectManager() {
        try {
            objectManager = ObjectManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            objectManager.load(mapName, resourceManager);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
    }

    public void initGuiManager() {
        guiManager = new GuiManager(player);
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

    public boolean isMouseMoved() {
        return isMouseMoved;
    }

    public boolean isMousePressed() {
        return isMouseLeftPressed || isMouseRightPressed;
    }

    public boolean isMouseReleased() {
        return isMouseLeftReleased || isMouseRightReleased;
    }

    public boolean isButtonPressed(int button) {
        return Mouse.isButtonDown(button);
    }

    /**
     * Is anyone is now in given cell
     */
    public boolean isAnyoneInCell(int cellX, int cellY) {

        try {
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                if (!GameCharacter.class.isInstance(object))
                    continue;

                GameCharacter character = (GameCharacter) object;

                Point characterPoint = LevelManager.getTilePointByCoordinates(character.getRealX(), character.getRealY());
                if (characterPoint.x == cellX && characterPoint.y == cellY) {
                    return true;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Return who is now in given cell
     */
    public GameCharacter getAnyoneInCell(int cellX, int cellY) {

        try {
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                if (!GameCharacter.class.isInstance(object))
                    continue;

                GameCharacter character = (GameCharacter) object;

                Point characterPoint = LevelManager.getTilePointByCoordinates(character.getRealX(), character.getRealY());
                if (characterPoint.x == cellX && characterPoint.y == cellY) {
                    return character;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * return array of Game Objects
     */
    public IGameObjects getObjects() {
        return objects;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
