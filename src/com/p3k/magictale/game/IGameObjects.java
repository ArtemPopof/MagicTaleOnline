package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameObjects extends Remote {
    GameObject get(int index) throws RemoteException;

    void set(GameObject object, int index) throws RemoteException;

    int size() throws RemoteException;

    void add(GameObject object) throws RemoteException;
}
