package server;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.Map.TileMap;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.physics.Collision;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.game.Characters.Bat;
import com.p3k.magictale.game.Characters.Bot;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import com.p3k.magictale.map.objects.ObjectManager;
import server.accounts.ActiveAccounts;
import server.network.Broadcaster;
import server.network.ControlHandler;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.p3k.magictale.engine.Constants.MAP_HEIGHT;
import static com.p3k.magictale.engine.Constants.MAP_WIDTH;
import static com.p3k.magictale.engine.Constants.TILE_SIZE;

public class ServerGame extends AbstractGame {
    private ServerSocket serverSocket;
    private final ActiveAccounts activeAccounts;
    private final Broadcaster broadcaster;
    private final ControlHandler handler;

    private Level levelManager;
    private ObjectManager objectManager;
    private final String mapName = "forest_v2";


    /**
     * container for all single-sprite objects in game
     * Integer - object id
     * GameObject - reference
     */
    protected final ConcurrentHashMap<Integer, GameObject> objects;
    private HashMap<Integer, ServerObject> serverObjects;

    private int playerIndex;


    private ServerGame() throws RemoteException, AlreadyBoundException, MalformedURLException {

        // TODO: init all
        serverObjects = new HashMap<>();
        this.objects = new ConcurrentHashMap<>();

        activeAccounts = ActiveAccounts.getInstance();
        broadcaster = Broadcaster.getInstance();

        // init controller
        LocateRegistry.createRegistry(1099);
        String name = "game";
        handler = new ControlHandler();
        Naming.bind(name, handler);

        initLevelManager();
        initObjectManager();

        initObjects();
/**
        try {
            initObjects();
        } catch (RemoteException | AlreadyBoundException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

 **/
    }

    public static AbstractGame getInstance() {
        if (instanceServer == null) {
            try {
                instanceServer = new ServerGame();
            } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        return instanceServer;
    }

    public HashMap<Integer, ServerObject> getServerObjects() {
        return serverObjects;
    }

    /**
     * один tick в игре, на сервере и клиенте должен иметь свои обработчики
     */
    @Override
    public void tick() {

        objectManager.update();

        for (Integer object : objects.keySet()) {

            GameObject obj = objects.get(object);

            obj.update();

        }

//        for (Integer key : this.objects.keySet()) {
//            GameObject object = this.objects.get(key);
//            object.update();
//
//            putServerObject(object.getId(), object.getSpriteId(), object.getX(), object.getY());
//        }

        //TODO DON'T DELETE IT. Need for render
//        TileMap[][] tilesOfLevel = null;
//        try {
//            tilesOfLevel = LevelManager.getInstance().getTileMap();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (tilesOfLevel != null) {
//            for (int h = 0; h < MAP_HEIGHT; ++h) {
//                for (int w = 0; w < MAP_WIDTH; ++w) {
//                    try {
//                        serverObjects.put(tilesOfLevel[w][h].getTiles().get(0).getId(),
//                                new ServerObject(tilesOfLevel[w][h].getTiles().get(0).getSpriteId(),
//                                        tilesOfLevel[w][h].getTiles().get(0).getX(),
//                                        tilesOfLevel[w][h].getTiles().get(0).getY()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        // отправка данных подключенным клиентам
        broadcaster.sendObjects(serverObjects.entrySet());

    }

    private void initObjects() {

//        Bot bat = new Bat(300, 300);

        Bot bot = new Bot(350, 280, 64, 64);
//        Bot bot2 = new Bot(330, 280, 64, 64);
//        Bat bot3 = new Bat(330, 240);
//        Bat bot4 = new Bat(350, 200);

//        objects.put(this.objects.size(), bat);
        objects.put(this.objects.size(), bot);
       // objects.put(this.objects.size(), bot2);
     //   objects.put(this.objects.size(), bot3);
//        objects.put(this.objects.size(), bot4);

    }

    private void initLevelManager() {
        try {
            this.levelManager = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            String mapName = "forest_v2";
            this.levelManager.loadServer(mapName);
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
            this.objectManager.loadServer(this.mapName);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
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
        this.objects.put(this.objects.size(), testBot3);

    }
    **/

    /**
     * Is anyone is now in given cell
     */
    public boolean isAnyoneInCell(int cellX, int cellY) {

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


        return false;
    }

    /**
     * Return who is now in given cell
     */
    public GameCharacter getAnyoneInCell(int cellX, int cellY) {

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

        return null;
    }

    /**
     * Return first GameCharacter founded in given rect
     *
     * @param startPoint - rectangle in pixels, where
     *                   should seek for characters
     */
    public GameCharacter getAnyoneInRect(Point startPoint, Point endPoint) {


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


        return null;
    }

    /**
     * return array of ClientGame Objects
     */
    public ConcurrentHashMap<Integer, GameObject> getObjects() {
        return this.objects;
    }

    public Player getPlayer() {
        return (Player) this.objects.get(this.playerIndex);
    }

    public void setPlayer(Player player) {
        this.objects.put(this.playerIndex, player);
    }

    /**
     * Creates specified gameObject
     */
    public void createGameObject(float x, float y, float width, float height, int r, int g, int b) {
        GameObject object = new GameObject(x, y, width, height, r, g, b);

        this.objects.put(this.objects.size(), object);

    }

    /**
     * Returns characters around specified point
     * radius - how far can be character from point
     * to be returned
     */
    public ArrayList<GameCharacter> getCharactersNearPoint(Point position, int radius) {

        ArrayList<GameCharacter> characters = new ArrayList<>();

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


        return characters;
    }

    public void spawnBot() {
        int x = ThreadLocalRandom.current().nextInt(0, (MAP_WIDTH - 1) * TILE_SIZE);
        int y = ThreadLocalRandom.current().nextInt(0, (MAP_HEIGHT - 1) * TILE_SIZE);

        Bot bot = new Bot(x, y, 64, 64);
        this.objects.put(this.objects.size(), bot);
    }

    public Player getNewPlayer() {

        Player newPlayer = new Player(500, 200);

        //objects.put(newPlayer.getId(), newPlayer);

        return newPlayer;
    }

    public void putServerObject(int id, int spriteId, float x, float y) {
        if (!serverObjects.containsKey(id)) {
//                serverObjects.put(object.getId(), new ServerObject(object.getSpriteId(), object.getX(), object.getY()));
            serverObjects.put(id, new ServerObject(spriteId, x, y));
        } else {
            ServerObject o = serverObjects.get(id);
            o.setIdResMan(spriteId);
            o.setX(x);
            o.setY(y);
        }
    }
}
