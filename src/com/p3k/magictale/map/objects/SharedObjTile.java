package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Objects.ObjTile;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by COMar-PC on 20.12.2016.
 */
public class SharedObjTile extends UnicastRemoteObject {
    private ObjTile[][][] objTile = null;

    protected SharedObjTile(int x, int y, int z) throws RemoteException {
        objTile = new ObjTile[x][y][z];
    }

    public ObjTile getObjTileByXYZ(int x, int y, int z) {
        return objTile[x][y][z];
    }

    public void setObjTileByXYZ(int x, int y, int z, ObjTile objTile) {
        this.objTile[x][y][z] = objTile;
    }
}
