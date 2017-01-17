package client;

import client.network.Receiver;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.Logger;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.gui.GuiManager;
import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.engine.sound.SoundSource;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import com.p3k.magictale.map.objects.ObjectInterface;
import com.p3k.magictale.map.objects.ObjectManager;
import common.remoteInterfaces.GameController;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * ClientGame routines
 * Created by artem96 on 03.12.16.
 */
public class ClientGame extends AbstractGame implements Constants {
    private static final boolean isDebug = true;

    private final String mapName = "forest_v2";
    private final Receiver receiver;
    //TODO Client objects
    private final int playerIndex;
    private final GameController controller;
    private Player player;
    private Map<Integer, ClientObject> clientObjects;
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

    private ClientGame(String ip, String nickname) throws RemoteException, NotBoundException {
        tickTimeMills = 20;
        System.out.println("Client tickrate: " + 1000 / this.tickTimeMills + " t/s");
        initDisplay();
        initGl();

        Display.sync(60);
        
        clientObjects = new TreeMap<>();

        initLevelManager();

        initSoundManager();

        initObjectManager();

        if (!System.getProperty("os.name").startsWith("Windows")) {
            try {
                Mouse.setNativeCursor(ResourceManager.getInstance(true).loadCursor("res/cursor.png"));
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        } else {
            Mouse.setGrabbed(true);
        }

        player = new Player();
        receiver = Receiver.getInstance();
        Registry registry = LocateRegistry.getRegistry(ip);
        controller = (GameController) registry.lookup("game");
        playerIndex = controller.signUp(nickname);
        if (playerIndex < 0) {
            System.exit(1);
        }
        this.player.setSprite(ResourceManager.getInstance(true).getSprite(playerIndex));


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
//        System.out.println("Client objects = " + clientObjects.size());

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            isRunning = false;
        }
        processInput();

        updateCamera();
        this.guiManager.update();

        render();
    }

    public void processInput() {
        // Mouse handle
        this.isMouseMoved = Mouse.getDX() != 0 || Mouse.getDY() != 0;
        this.isMouseLeftReleased = this.isMouseLeftPressed && !Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightReleased = this.isMouseRightPressed && !Mouse.isButtonDown(MOUSE_BTN_RIGHT);
        this.isMouseLeftPressed = Mouse.isButtonDown(MOUSE_BTN_LEFT);
        this.isMouseRightPressed = Mouse.isButtonDown(MOUSE_BTN_RIGHT);

        try {
            if (!player.isDead()) {
                if (isMouseLeftPressed) {
                    controller.setPlayerState(GameController.State.ATTACK);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                    controller.setPlayerDirection(GameController.Direction.UP);
                    controller.setPlayerState(GameController.State.MOVE);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                    controller.setPlayerDirection(GameController.Direction.DOWN);
                    controller.setPlayerState(GameController.State.MOVE);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                    controller.setPlayerDirection(GameController.Direction.LEFT);
                    controller.setPlayerState(GameController.State.MOVE);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                    controller.setPlayerDirection(GameController.Direction.RIGHT);
                    controller.setPlayerState(GameController.State.MOVE);
                } else {
                    controller.setPlayerState(GameController.State.WAIT);
                }
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        levelManager.render();

        if (clientObjects != null) {
//            for (Integer key : clientObjects.keySet()) {
//                ClientObject clientObject = clientObjects.get(key);
//                clientObject.render();
//                clientObject.renderPoint();
//            }
            clientObjects.values()
                    .stream()
                    .sorted((o1, o2) -> (int) (o2.getY() - o1.getY()))
                    .forEach(ClientObject::render);
        }

//        this.objectManager.render();
//        this.objectManager.render(0);
//        this.objectManager.render(1);

//        for (int i = 0; i < this.objects.size(); i++) {
//            GameObject object = this.objects.get(i);
//            object.render();
//        }

//        this.objectManager.render(2);

        this.guiManager.render();

        Display.update();
    }

    @Override
    protected void finalize() throws Throwable {
        Logger.log("Destroying resources", Logger.DEBUG);

        Display.destroy();
        Keyboard.destroy();
        SoundManager.destroy();
        super.finalize();
    }

    /**
     * private void initObjects() throws RemoteException, AlreadyBoundException, MalformedURLException, NotBoundException {
     * <p>
     * Player player = new Player(100, 520);
     * <p>
     * synchronized (this.objects) {
     * this.playerIndex = this.objects.size();
     * this.objects.put(this.playerIndex, player);
     * }
     * <p>
     * // test bot
     * //Bot testBot = new Bot(500, 400, 64, 64);
     * //objects.add(testBot);
     * <p>
     * // test bot
     * // Bot testBot2 = new Bot(200, 354, 64, 64);
     * //objects.add(testBot2);
     * <p>
     * // test bot
     * Bot testBot3 = new Bot(252, 272, 64, 64);
     * <p>
     * //test bat
     * Bot batBot = new Bat(270, 280);
     * <p>
     * this.objects.put(this.objects.size(), testBot3);
     * this.objects.put(this.objects.size(), batBot);
     * <p>
     * }
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
        bgmSound.setLevel(0.8f).play("main_theme.wav");
        envSound.setLevel(0.8f).play("wind.wav");
    }

    private void initLevelManager() {
        try {
            this.resourceManager = ResourceManager.getInstance(true);
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
        this.guiManager = new GuiManager(this.player);
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

    public void updateCamera() {
        ClientObject player = clientObjects.get(playerIndex);
        if (player == null) return;
        Sprite playerSprite = player.getSprite();

        this.cameraX = player.getX() - 400 + playerSprite.getWidth() / 2;
        this.cameraY = player.getY() - 300 + playerSprite.getHeight() / 2;
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
//        if (!clientObjects.containsKey(id)) {
//            System.out.println("New object " + id + " " + object.getIdResMan() + " total " + clientObjects.size() +
//            " coord: " + object.getX() + " " + object.getY());
//        }
        if (!clientObjects.containsKey(id) || clientObjects.get(id).getTimestamp() <= object.getTimestamp()) {
            object.setX(object.getX() + object.getSprite().getWidth() / 2);
            object.setY(object.getY() - object.getSprite().getHeight());
            clientObjects.put(id, object);
        }
    }


    public GameController getController() {
        return controller;
    }

    public void updatePlayer(Player player) {
        if (this.player.getTimestamp() <= player.getTimestamp()) {
            this.player.setTimestamp(player.getTimestamp());
            this.player.setCurrentHealth(player.getCurrentHealth());
            this.player.setMaxHealth(player.getMaxHealth());
            this.player.setSpeed(player.getSpeed());
            this.player.setAttack(player.getAttack());
            this.player.setDead(player.isDead());
            this.player.setCurrentLevel(player.getCurrentLevel());
            this.player.setXpForNextLevel(player.getXpForNextLevel());
            this.player.setXp(player.getXp());
            ClientObject co = clientObjects.get(playerIndex);
            if (co != null) {
                this.player.setSprite(co.getSprite());
            }
        }
    }
}
