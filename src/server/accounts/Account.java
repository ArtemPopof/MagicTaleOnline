package server.accounts;

import com.p3k.magictale.game.Characters.Player;
import server.ServerGame;

public class Account {
    private final Player playerObject;
    private final String nickname;
    private long lastAccessTime;
    private String ip;

    public Account(String nickname) {
        this.nickname = nickname;

        playerObject = ((ServerGame) ServerGame.getInstance()).getNewPlayer();
        lastAccessTime = System.currentTimeMillis();
    }

    public String getNickname() {
        return nickname;
    }

    public Player getPlayerObject() {
        return playerObject;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }
}
