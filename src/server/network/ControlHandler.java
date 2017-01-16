package server.network;

import com.p3k.magictale.engine.Logger;
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
     * @return current state of your player
     * @throws RemoteException java rmi exception
     */
    @Override
    public State getPlayerState() throws RemoteException {
        try {
            switch (ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject().getState()) {
                case 1:
                    return State.RIGHT_MOVE_STATE;
                case 2:
                    return State.LEFT_MOVE_STATE;
                case 3:
                    return State.UP_MOVE_STATE;
                case 4:
                    return State.DOWN_MOVE_STATE;
                case 5:
                    return State.DEATH_STATE;
                case 6:
                    return State.LEFT_ATTACK_STATE;
                case 7:
                    return State.UP_ATTACK_STATE;
                case 8:
                    return State.RIGHT_ATTACK_STATE;
                case 9:
                    return State.DOWN_ATTACK_STATE;
                default:
                    return State.WAITING_STATE;
            }
        } catch (ServerNotActiveException e) {
            Logger.log(e.getMessage(), Logger.DEBUG);
        }
        return State.WAITING_STATE;
    }

    /**
     * @param state будет установлен твоему игроку
     * @throws RemoteException java rmi exception
     */
    @Override
    public void setPlayerState(State state) throws RemoteException {
        try {
            Player player = ActiveAccounts.getInstance().getAccount(getClientHost()).getPlayerObject();

            switch (state) {
                case WAITING_STATE:
                    player.setState(0);
                    break;
                case RIGHT_MOVE_STATE:
                    player.setState(1);
                    break;
                case LEFT_MOVE_STATE:
                    player.setState(2);
                    break;
                case UP_MOVE_STATE:
                    player.setState(3);
                    break;
                case DOWN_MOVE_STATE:
                    player.setState(4);
                    break;
                case DEATH_STATE:
                    player.setState(5);
                    break;
                case LEFT_ATTACK_STATE:
                    player.setState(6);
                    break;
                case UP_ATTACK_STATE:
                    player.setState(7);
                    break;
                case RIGHT_ATTACK_STATE:
                    player.setState(8);
                    break;
                case DOWN_ATTACK_STATE:
                    player.setState(9);
                    break;
                default:
                    break;
            }
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }
}
