package server.network;

import com.p3k.magictale.game.Characters.Player;
import common.remoteInterfaces.GameController;
import server.accounts.Account;
import server.accounts.ActiveAccounts;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
    public ControlHandler() throws RemoteException {
    }


    /**
     * @param nickname - player nickname
     * @throws RemoteException java rmi exception
     */
    @Override
    public void signUp(String nickname) throws RemoteException {
        try {
            ActiveAccounts.getInstance().setEnable(getClientHost(), nickname);
            System.out.println("New client connected " + nickname + " " + getClientHost());
        } catch (ServerNotActiveException e) {
            System.err.println(e.getMessage());
        }
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
        try {
            Player player = ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject();
            return player.getCurrentHealth();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
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
