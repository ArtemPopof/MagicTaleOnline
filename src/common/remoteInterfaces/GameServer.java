package common.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Aleksandr Ploskov
 */
public interface GameServer extends Remote {
    /**
     * @return rmi address of player
     * @throws RemoteException java rmi exception
     */
    String signUp() throws RemoteException;
}