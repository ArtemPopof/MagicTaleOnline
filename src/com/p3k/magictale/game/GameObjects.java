package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For server syncronization
 * <p>
 * Created by artem96 on 20.12.16.
 */
public class GameObjects extends UnicastRemoteObject implements IGameObjects {

    private final ConcurrentHashMap<Integer, GameObject> objects;

    public GameObjects() throws RemoteException {
        objects = new ConcurrentHashMap<>();
    }

    public GameObject get(int index) throws RemoteException {
        return objects.get(index);
    }

    public void set(GameObject object, int index) throws RemoteException {
        this.objects.put(index, object);
    }

    public int size() throws RemoteException {
        return objects.size();
    }

    public void add(GameObject object) throws RemoteException {
        synchronized (objects) {
            objects.put(objects.size(), object);
        }
    }


}
