package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Objects.ObjTile;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISharedObjTile extends Remote {
    ObjTile getObjTileByXYZ(int x, int y, int z) throws RemoteException;

    void setObjTileByXYZ(int x, int y, int z, ObjTile objTile) throws RemoteException;
}
