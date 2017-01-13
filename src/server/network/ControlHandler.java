package server.network;

import common.remoteInterfaces.GameController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Данный класс предоставляет реализацию удаленного интерфейса на сервере, каждый из данных методов должен
 * создавать некоторую задачу, которую потом сможет выполнить mainloop сервера, очередь задач находится в
 * {@link server.ServerGame}
 */
public class ControlHandler extends UnicastRemoteObject implements GameController {
    /**
     * Creates and exports a new UnicastRemoteObject object using an
     * anonymous port.
     * <p>
     * <p>The object is exported with a server socket
     *
     * @throws RemoteException if failed to export object
     * @since JDK1.1
     */
    protected ControlHandler() throws RemoteException {
    }


    /**
     * @param nickname - player nickname
     * @throws RemoteException java rmi exception
     */
    @Override
    public void signUp(String nickname) throws RemoteException {

    }

    /**
     * @param x x coordinate on map
     * @param y y coordinate on map
     * @throws RemoteException java rmi exception
     */
    @Override
    public void click(float x, float y) throws RemoteException {

    }

    /**
     * @return player current move direction
     * @throws RemoteException java rmi exception
     */
    @Override
    public MoveDirection getMoveDirection() throws RemoteException {
        return null;
    }

    /**
     * @param state player move direction
     * @throws RemoteException java rmi exception
     */
    @Override
    public void setMoveDirection(MoveDirection state) throws RemoteException {

    }

    /**
     * @return player current health
     * @throws RemoteException java rmi exception
     */
    @Override
    public int getHealth() throws RemoteException {
        return 0;
    }

    /**
     * @return player max health
     * @throws RemoteException java rmi exception
     */
    @Override
    public int getMaxHealth() throws RemoteException {
        return 0;
    }
}