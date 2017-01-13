package server;

import client.ClientMessage;
import com.p3k.magictale.game.AbstractGame;
import com.p3k.magictale.game.Characters.Player;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;

import java.net.ServerSocket;
import java.util.LinkedList;

public class ServerGame extends AbstractGame {
    private ServerSocket serverSocket;
    private Level levelManager;

    private LinkedList<ClientMessage> messagesToClient;

    private ServerGame() {
        // TODO: init all
        initLevelManager();
    }

    public static AbstractGame getInstance() {
        if (instanceServer == null) {
            instanceServer = new ServerGame();
        }

        return instanceServer;
    }

    public LinkedList<ClientMessage> getMessagesToClient() {
        return messagesToClient;
    }

    /**
     * один tick в игре, на сервере и клиенте должен иметь свои обработчики
     */
    @Override
    public void tick() {

    }

    private void initLevelManager() {
        messagesToClient = new LinkedList<>();

        try {
            this.levelManager = LevelManager.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing levelManager manager: " + e);
        }

        try {
            String mapName = "forest_v2";
            this.levelManager.loadServer(mapName, messagesToClient);
        } catch (Exception e) {
            System.err.println("Error render levelManager manager: " + e);
        }
    }

    public Player getNewPlayer() {
        return null;
    }
}
