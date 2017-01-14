package server;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.Map.TileMap;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import server.accounts.ActiveAccounts;
import server.network.Broadcaster;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class ServerGame extends AbstractGame {
    private ServerSocket serverSocket;
    private Level levelManager;
    private final ActiveAccounts activeAccounts;
    private final Broadcaster broadcaster;

    private HashMap<Integer, ServerObject> serverObjects;

    private ServerGame() {
        // TODO: init all
        serverObjects = new HashMap<>();

        activeAccounts = ActiveAccounts.getInstance();
        broadcaster = Broadcaster.getInstance();

        initLevelManager();
    }

    public static AbstractGame getInstance() {
        if (instanceServer == null) {
            instanceServer = new ServerGame();
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
        //TODO DON'T DELETE IT. Need for render
        TileMap[][] tilesOfLevel = null;
        try {
            tilesOfLevel = LevelManager.getInstance().getTileMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tilesOfLevel != null) {
            for (int h = 0; h < Constants.MAP_HEIGHT; ++h) {
                for (int w = 0; w < Constants.MAP_WIDTH; ++w) {
                    try {
                        serverObjects.put(tilesOfLevel[w][h].getTiles().get(0).getId(),
                                new ServerObject(tilesOfLevel[w][h].getTiles().get(0).getSpriteId(),
                                        (int)tilesOfLevel[w][h].getTiles().get(0).getX(),
                                        (int)tilesOfLevel[w][h].getTiles().get(0).getY())) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // отключение "отвалившихся" клиентов
        activeAccounts.tick();
        // отправка данных подключенным клиентам
        broadcaster.sendObjects(serverObjects.values());

    }

    private void initLevelManager() {
        try {
            this.levelManager = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            String mapName = "forest_v2";
            this.levelManager.loadServer(mapName, serverObjects);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }

        //TODO DELETE IT. DEBUG
        TileMap[][] tilesOfLevel = null;
        try {
            tilesOfLevel = LevelManager.getInstance().getTileMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tilesOfLevel != null) {
            for (int h = 0; h < Constants.MAP_HEIGHT; ++h) {
                for (int w = 0; w < Constants.MAP_WIDTH; ++w) {
                    try {
                        serverObjects.put(tilesOfLevel[w][h].getTiles().get(0).getId(),
                                new ServerObject(tilesOfLevel[w][h].getTiles().get(0).getSpriteId(),
                                        (int)tilesOfLevel[w][h].getTiles().get(0).getX(),
                                        (int)tilesOfLevel[w][h].getTiles().get(0).getY())) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Player getNewPlayer() {
        return null;
    }
}
