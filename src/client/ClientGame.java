package client;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.gui.GuiManager;
import com.p3k.magictale.engine.physics.Collision;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.game.Characters.Bot;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.game.GameObjects;
import com.p3k.magictale.game.IGameObjects;
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
import java.util.ArrayList;

/**
 * ClientGame routines
 * Created by artem96 on 03.12.16.
 */
public class ClientGame extends AbstractGame implements Constants {

    private final String mapName = "forest_v2";
    private IGameObjects objects;

    //private Player player;
    private int playerIndex;
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

    private ClientGame() {
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
            this.cursor = ResourceManager.getInstance().loadCursor("res/cursor.png");

            Mouse.setNativeCursor(this.cursor);
        } catch (Exception e) {
            System.err.println("Error loading cursor");
        }

        initSoundManager();

        try {
            initObjects();
        } catch (RemoteException | AlreadyBoundException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        initObjectManager();

        initGuiManager();
    }

    public static AbstractGame getInstance() {
        if (instance == null) {
            instance = new ClientGame();
        }

        return instance;
    }


    public void processInput() {

        try {
            for (int i = 0; i < this.objects.size(); i++) {
                GameObject object = this.objects.get(i);

                if (GameCharacter.class.isInstance(object)) {
                    GameCharacter character = (GameCharacter) object;
                    character.processInput();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Mouse handle
        this.isMouseMoved = Mouse.getDX() != 0 || Mouse.getDY() != 0;
        this.isMouseLeftReleased = this.isMouseLeftPressed && !Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightReleased = this.isMouseRightPressed && !Mouse.isButtonDown(MOUSE_BTN_RIGHT);
        this.isMouseLeftPressed = Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightPressed = Mouse.isButtonDown(MOUSE_BTN_RIGHT);
    }

    public void update() {

//        levelManager.update();
        if (System.getenv("IP") == null) {
            try {
                for (int i = 0; i < this.objects.size(); i++) {
                    GameObject object = this.objects.get(i);
                    object.update();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        this.guiManager.update();
    }

    public void render() {

//        levelManager.render();

        this.objectManager.render(0);
        this.objectManager.render(1);

        try {
            for (int i = 0; i < this.objects.size(); i++) {
                GameObject object = this.objects.get(i);
                object.render();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        this.objectManager.render(2);

        this.guiManager.render();
    }

    public void cleanUp() {

    }

    private void initObjects() throws RemoteException, AlreadyBoundException, MalformedURLException, NotBoundException {
        String remoteIP = System.getenv("IP");
        if (remoteIP == null) {
            this.objects = new GameObjects();
            Naming.bind("GameObjects", this.objects);
            System.out.println("RMI backend for objects created");
        } else {
            Registry registry = LocateRegistry.getRegistry(remoteIP);
            this.objects = (IGameObjects) registry.lookup("GameObjects");
            System.out.println("remote objects in use");
        }

        Player player = new Player(100, 520);

        this.playerIndex = this.objects.add(player);

        // test bot
        //Bot testBot = new Bot(500, 400, 64, 64);
        //objects.add(testBot);

        // test bot
       // Bot testBot2 = new Bot(200, 354, 64, 64);
        //objects.add(testBot2);

        // test bot
        Bot testBot3 = new Bot(252, 272, 64, 64);
        this.objects.add(testBot3);

    }

    private void initSoundManager() {
        try {
            this.soundManager = SoundManager.getInstance();

            this.soundManager.registerSound("main_theme.wav");
            this.soundManager.registerSound("wind.wav");
            this.soundManager.registerSound("user/baphomet_breath.wav");
            this.soundManager.registerSound("user/attack_axe.wav");
        } catch (Exception e) {
            System.err.println("Error initializing sound manager: " + e);
        }


        try {
            this.bgmSound = new SoundSource(null, true);
            this.envSound = new SoundSource(null, true);

        } catch (Exception e) {
            System.err.println("Error loading bgm sounds: " + e);
        }

        // Must be moved to more appropriate place?
        //  bgmSound.setLevel(0.8f).play("main_theme.wav");
        //  envSound.setLevel(0.8f).play("wind.wav");
    }

    private void initLevelManager() {
        try {
            this.resourceManager = ResourceManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.levelManager = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            this.levelManager.load(this.mapName, this.resourceManager);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
    }

    private void initObjectManager() {
        try {
            this.objectManager = ObjectManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            this.objectManager.load(this.mapName, this.resourceManager);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
    }

    public void initGuiManager() {
        try {
            this.guiManager = new GuiManager((Player) this.objects.get(this.playerIndex));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Level getLevelManager() {
        return this.levelManager;
    }

    public float getCameraX() {
        return this.cameraX;
    }

    public void setCameraX(float cameraX) {
        this.cameraX = cameraX;
    }

    public float getCameraY() {
        return this.cameraY;
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    public void moveCamera(float x, float y) {
        this.cameraX += x;
        this.cameraY += y;
    }

    public boolean isMouseMoved() {
        return this.isMouseMoved;
    }

    public boolean isMousePressed() {
        return this.isMouseLeftPressed || this.isMouseRightPressed;
    }

    public boolean isMouseReleased() {
        return this.isMouseLeftReleased || this.isMouseRightReleased;
    }

    public boolean isButtonPressed(int button) {
        return Mouse.isButtonDown(button);
    }

    /**
     * Is anyone is now in given cell
     */
    public boolean isAnyoneInCell(int cellX, int cellY) {

        try {
            for (int i = 0; i < this.objects.size(); i++) {
                GameObject object = this.objects.get(i);
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
            for (int i = 0; i < this.objects.size(); i++) {
                GameObject object = this.objects.get(i);
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
     * Return first GameCharacter founded in given rect
     * @param startPoint - rectangle in pixels, where
     *                  should seek for characters
     */
    public GameCharacter getAnyoneInRect(Point startPoint, Point endPoint) {

        try {

            for (int i = 0; i < this.objects.size(); i++) {
                if (!GameCharacter.class.isInstance(this.objects.get(i))) {
                    continue;
                }

                GameCharacter character = (GameCharacter) this.objects.get(i);

                Point charPoint = new Point((int) character.getRealX(), (int) character.getRealY());

                if (Collision.isPointInRect(charPoint, startPoint, endPoint)) {
                    return character;
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * return array of ClientGame Objects
     */
    public IGameObjects getObjects() {
        return this.objects;
    }

    public Player getPlayer() {
        try {
            return (Player) this.objects.get(this.playerIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setPlayer(Player player) {
        try {
            this.objects.set(player, this.playerIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates specified gameObject
     */
    public void createGameObject(float x, float y, float width, float height, int r, int g, int b) {
        GameObject object = new GameObject(x, y, width, height, r, g, b);

        try {
            this.objects.add(object);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns characters around specified point
     * radius - how far can be character from point
     * to be returned
     */
    public ArrayList<GameCharacter> getCharactersNearPoint(Point position, int radius) {

        ArrayList<GameCharacter> characters = new ArrayList<>();

        try {
            for (int i = 0; i < this.objects.size(); i++) {
                Object object;
                object = this.objects.get(i);


                if (!GameCharacter.class.isInstance(object)) continue;

                GameCharacter character = (GameCharacter) object;

                if (Math.abs(position.x - character.getRealX()) < radius
                        && Math.abs(position.y - character.getRealY()) < radius) {

                    characters.add(character);

                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return characters;
    }

}
