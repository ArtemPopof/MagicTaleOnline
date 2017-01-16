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
     * @return current state of your player
     * @throws RemoteException java rmi exception
     */
    State getPlayerState() throws RemoteException;


    /**
     * @param state будет установлен твоему игроку
     * @throws RemoteException java rmi exception
     */
    void setPlayerState(State state) throws RemoteException;

    enum State {
        WAITING_STATE,
        RIGHT_MOVE_STATE,
        LEFT_MOVE_STATE,
        UP_MOVE_STATE,
        DOWN_MOVE_STATE,
        DEATH_STATE,
        LEFT_ATTACK_STATE,
        UP_ATTACK_STATE,
        RIGHT_ATTACK_STATE,
        DOWN_ATTACK_STATE
    };
}