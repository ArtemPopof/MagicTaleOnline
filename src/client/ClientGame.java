package client;

import client.network.Receiver;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.gui.GuiManager;
import com.p3k.magictale.engine.physics.Collision;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.game.Characters.Bat;
import com.p3k.magictale.game.Characters.Bot;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import com.p3k.magictale.map.objects.ObjectInterface;
import com.p3k.magictale.map.objects.ObjectManager;
import common.remoteInterfaces.GameController;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import server.ServerGame;
import server.ServerObject;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

/**
 * ClientGame routines
 * Created by artem96 on 03.12.16.
 */
public class ClientGame extends AbstractGame implements Constants {
    private static final boolean isDebug = true;

    private final String mapName = "forest_v2";
    private final Receiver receiver;
    private Map<Integer, ClientObject> clientObjects;
    //TODO Client objects
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
    private boolean isInFullScreenMode = false;
    private final GameController controller;

    private int score = 0;

    private Cursor cursor;

    private ClientGame(String ip, String nickname) throws RemoteException, NotBoundException {
        initDisplay();
        initGl();

        Display.sync(60);

        receiver = Receiver.getInstance();
        Registry registry = LocateRegistry.getRegistry(ip);
        controller = (GameController) registry.lookup("game");
        controller.signUp(nickname);

        clientObjects = new TreeMap<>();

        initLevelManager();

        // INITIALIZING CURSOR
        try {
            this.cursor = ResourceManager.getInstance().loadCursor("res/cursor.png");

            Mouse.setNativeCursor(this.cursor);
        } catch (Exception e) {
            System.err.println("Error loading cursor");
        }

        initSoundManager();

        initObjectManager();

        initGuiManager();

        System.out.println("HERE ClientGame loaded");
    }

    public static AbstractGame getInstance() {
        if (instance == null) {
            System.err.println("There are no ClientGame created");
            System.exit(1);
        }

        return instance;
    }

    public static ClientGame createInstance(String ip, String nickname) {
        if (instance == null) {
            try {
                instance = new ClientGame(ip, nickname);
            } catch (RemoteException | NotBoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        return (ClientGame) instance;
    }

    public static boolean isDebug() {
        return ClientGame.isDebug;
    }

    /**
     * один tick в игре, на сервере и клиенте должен иметь свои обработчики
     */
    @Override
    public void tick() {
        // получение объектов с из очереди полученных с сервера
        receiver.tick();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            isRunning = false;
        }
        processInput();

        // а это что-то
//        this.guiManager.update();

        render();
    }

    public void processInput() {

//        for (int i = 0; i < this.objects.size(); i++) {
//            GameObject object = this.objects.get(i);
//
//            if (GameCharacter.class.isInstance(object)) {
//                GameCharacter character = (GameCharacter) object;
//                character.processInput();
//            }
//        }


        // Mouse handle
        this.isMouseMoved = Mouse.getDX() != 0 || Mouse.getDY() != 0;
        this.isMouseLeftReleased = this.isMouseLeftPressed && !Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightReleased = this.isMouseRightPressed && !Mouse.isButtonDown(MOUSE_BTN_RIGHT);
        this.isMouseLeftPressed = Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightPressed = Mouse.isButtonDown(MOUSE_BTN_RIGHT);
    }

//    public void update() {
//
//        synchronized (this.objects) {
//            for (Integer key : this.objects.keySet()) {
//                GameObject object = this.objects.get(key);
//                object.update();
//            }
//        }
//
//        HashMap<Integer, ServerObject> serverObjects = ((ServerGame) ServerGame.getInstance()).getServerObjects();
//        if (serverObjects != null) {
//            for (Integer key : serverObjects.keySet()) {
//                ClientObject insObj = clientObjects.get(key);
//                if (insObj != null) {
//                    insObj.setIdResMan(serverObjects.get(key).getIdResMan());
//                    insObj.setX(serverObjects.get(key).getX());
//                    insObj.setY(serverObjects.get(key).getY());
//                    insObj.setSprite(resourceManager.getSprite(insObj.getIdResMan()));
//                    clientObjects.put(key, insObj);
//                } else {
//                    try {
//                        insObj = new ClientObject(serverObjects.get(key).getIdResMan(),
//                                serverObjects.get(key).getX(), serverObjects.get(key).getY());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    clientObjects.put(key, insObj);
//                }
//            }
//        }
//        this.guiManager.update();
//    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        levelManager.render();

        if (clientObjects != null) {
            for (Integer key : clientObjects.keySet()) {
                ClientObject clientObject = clientObjects.get(key);
                clientObject.render();
            }
        }

//        this.objectManager.render();
//        this.objectManager.render(0);
//        this.objectManager.render(1);

//        for (int i = 0; i < this.objects.size(); i++) {
//            GameObject object = this.objects.get(i);
//            object.render();
//        }

//        this.objectManager.render(2);

//        this.guiManager.render();

        Display.update();
    }

    @Override
    protected void finalize() throws Throwable {
        Display.destroy();
        Keyboard.destroy();
        SoundManager.destroy();
        super.finalize();
    }

    /**
    private void initObjects() throws RemoteException, AlreadyBoundException, MalformedURLException, NotBoundException {

        Player player = new Player(100, 520);

        synchronized (this.objects) {
            this.playerIndex = this.objects.size();
            this.objects.put(this.playerIndex, player);
        }

        // test bot
        //Bot testBot = new Bot(500, 400, 64, 64);
        //objects.add(testBot);

        // test bot
        // Bot testBot2 = new Bot(200, 354, 64, 64);
        //objects.add(testBot2);

        // test bot
        Bot testBot3 = new Bot(252, 272, 64, 64);

        //test bat
        Bot batBot = new Bat(270, 280);

        this.objects.put(this.objects.size(), testBot3);
        this.objects.put(this.objects.size(), batBot);

    }
    **/

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
            this.levelManager.loadClient(this.mapName, this.resourceManager);
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
            this.objectManager.loadClient(this.mapName, this.resourceManager);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
    }

    private void initGuiManager() {

//        this.guiManager = new GuiManager((Player) this.objects.get(this.playerIndex));

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

    public void initDisplay() {

        try {
            if (this.isInFullScreenMode) {
                org.lwjgl.opengl.DisplayMode[] modes = Display.getAvailableDisplayModes();
                org.lwjgl.opengl.DisplayMode max = modes[0];

                for (org.lwjgl.opengl.DisplayMode mode : modes) {
                    System.out.println("test mode: " + mode);
                    if (mode.getWidth() * mode.getHeight() * mode.getFrequency() >
                            max.getWidth() * max.getHeight() * max.getFrequency()) {
                        max = mode;
                    }
                }

                System.out.println("choose mode: " + max);
                Display.setDisplayModeAndFullscreen(max);
            } else {
                Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
            }

            Display.create();
            Keyboard.create();
            Display.setVSyncEnabled(true);
        } catch (LWJGLException ex) {
//            Logger.getLogger(MagicMain.class.getName()).log(java.util.logging.Level.SEVERE, "Something went wrong in initDisplay()");
            System.out.println("CHANGE To Logger");
        }
    }

    public void initGl() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_DEPTH_TEST);
        glClearColor(0, 0, 0, 0);

        glEnable(GL_TEXTURE_2D);

    }

    // обновляем (или добавляем) полученный объект в список клиентских объектов
    public void updateObject(int id, ClientObject object) {
        if (!clientObjects.containsKey(id) || clientObjects.get(id).getTimestamp() < object.getTimestamp()) {
            clientObjects.put(id, object);
        }
    }


    public GameController getController() {
        return controller;
    }

}
