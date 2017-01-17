package server.network;

import com.p3k.magictale.game.Characters.Player;
import common.remoteInterfaces.GameController;
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
     * @param nickname - player nickname or -1 if something went wrong
     * @return id of your player
     * @throws RemoteException java rmi exception
     */
    @Override
    public int signUp(String nickname) throws RemoteException {
        try {
            ActiveAccounts.getInstance().setEnable(getClientHost(), nickname);
            System.out.println("New client connected " + nickname + " " + getClientHost());
            return ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject().getId();
        } catch (ServerNotActiveException e) {
            System.err.println(e.getMessage());
        }
        return -1;
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
     * @param state будет установлен твоему игроку
     * @throws RemoteException java rmi exception
     */
    @Override
    public void setPlayerState(State state) throws RemoteException {
        try {
            Player player = ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject();

            player.setCharacterState(state);
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param direction указание направления для человечка
     * @throws RemoteException java rmi exception
     */
    @Override
    public void setPlayerDirection(Direction direction) throws RemoteException {
        try {
            Player player = ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject();

            player.setDirection(direction);
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }
}
