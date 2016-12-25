package server;

import com.p3k.magictale.engine.graphics.GameObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer
        extends UnicastRemoteObject
        implements common.remoteInterfaces.GameServer {
    /**
     * global GameServer object
     */
    private static GameServer server;

    /**
     * server global run state
     */
    private boolean isRunning;

    /**
     * mainloop tick time between update actions
     */
    private long tickTimeMillis;

    /**
     * Container for all objects in common.game
     * Integer - object id
     * GameObject - object reference
     */
    private ConcurrentHashMap<Integer, GameObject> objects;

    /**
     * @throws RemoteException - java rmi exception
     */
    private GameServer() throws RemoteException {
        this.isRunning = true;
        this.tickTimeMillis = 40;
        System.out.println("Server tickrate: " + 1000 / this.tickTimeMillis + " t/s");

        // TODO: init level manager
        // TODO: init objects
        // TODO: init object manager
    }

    /**
     * @return global GameServer object
     * @throws RemoteException - java rmi exception
     */
    public static synchronized GameServer getInstance() throws RemoteException {
        if (server == null) {
            server = new GameServer();
        }
        return server;
    }

    /**
     * server mainloop, runs every 'tickTimeMillis' milliseconds
     */
    public void mainLoop() {
        long lastRunMillis = System.currentTimeMillis();
        while (this.isRunning) {
            if (System.currentTimeMillis() - lastRunMillis < this.tickTimeMillis) {
                continue;
            }
            lastRunMillis += this.tickTimeMillis;

            // TODO: do something
        }
    }

    /**
     * @return rmi address of player
     * @throws RemoteException java rmi exception
     */
    @Override
    public String signUp() throws RemoteException {
        return null;
    }
}
