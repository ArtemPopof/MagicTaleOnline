package common.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Aleksandr Ploskov
 */
public interface GameController extends Remote {
    /**
     * @param nickname - player nickname
     * @return id of your player
     * @throws RemoteException java rmi exception
     */
    int signUp(String nickname) throws RemoteException;

    /**
     * @param x x coordinate on map
     * @param y y coordinate on map
     * @throws RemoteException java rmi exception
     */
    void click(float x, float y) throws RemoteException;

    /**
     * @param state будет установлен твоему игроку
     * @throws RemoteException java rmi exception
     */
    void setPlayerState(State state) throws RemoteException;

    /**
     * @param direction указание направления для человечка
     * @throws RemoteException java rmi exception
     */
    void setPlayerDirection(Direction direction) throws RemoteException;

    enum State {
        WAIT,
        MOVE,
        ATTACK
    };

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}