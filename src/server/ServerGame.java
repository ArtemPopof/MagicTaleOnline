package server;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.game.AbstractGame;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerGame extends AbstractGame {
    private ServerSocket serverSocket;

    private ServerGame() {
        // TODO: init all
    }

    public static AbstractGame getInstance() {
        if (instance == null) {
            instance = new ServerGame();
        }

        return instance;
    }

    /**
     * один tick в игре, на сервере и клиенте должен иметь свои обработчики
     */
    @Override
    public void tick() {

    }
}
