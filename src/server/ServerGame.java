package server;

import com.p3k.magictale.game.AbstractGame;

public class ServerGame extends AbstractGame {
    private ServerGame() {

    }

    public static AbstractGame getInstance() {
        if (instance == null) {
            instance = new ServerGame();
        }

        return instance;
    }
}
