package common.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Aleksandr Ploskov
 */
public interface Player extends Remote {
    /**
     * @param x x coordinate on map
     * @param y y coordinate on map
     * @throws RemoteException java rmi exception
     */
    void click(float x, float y) throws RemoteException;

    /**
     * @return player current move direction
     * @throws RemoteException java rmi exception
     */
    MoveDirection getMoveDirection() throws RemoteException;

    /**
     * @param state player move direction
     * @throws RemoteException java rmi exception
     */
    void setMoveDirection(MoveDirection state) throws RemoteException;

    /**
     * @return player current health
     * @throws RemoteException java rmi exception
     */
    int getHealth() throws RemoteException;

    /**
     * @return player max health
     * @throws RemoteException java rmi exception
     */
    int getMaxHealth() throws RemoteException;

    enum MoveDirection {
        NONE,
        MOVE_RIGHT,
        MOVE_LEFT,
        MOVE_UP,
        MOVE_DOWN
    }
}
