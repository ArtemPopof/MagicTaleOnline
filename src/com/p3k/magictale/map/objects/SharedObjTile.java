package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Objects.ObjTile;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by COMar-PC on 20.12.2016.
 */
public class SharedObjTile extends UnicastRemoteObject implements ISharedObjTile {
    //    private ObjTile[][][] objTile = null;
    private ConcurrentHashMap<TripleKey, ObjTile> objTile;

    protected SharedObjTile(int x, int y, int z) throws RemoteException {
//        objTile = new ObjTile[x][y][z];
        objTile = new ConcurrentHashMap<>();
    }

    public ObjTile getObjTileByXYZ(int x, int y, int z) throws RemoteException {
        return objTile.get(new TripleKey(x, y, z));
    }

    public void setObjTileByXYZ(int x, int y, int z, ObjTile objTile) throws RemoteException {
        this.objTile.put(new TripleKey(x, y, z), objTile);
    }
}

class TripleKey {
    final int x;
    final int y;
    final int z;

    public TripleKey(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(final Object O) {
        if (!(O instanceof TripleKey)) return false;
        if (((TripleKey) O).x != x) return false;
        if (((TripleKey) O).y != y) return false;
        if (((TripleKey) O).z != z) return false;
        return true;
    }

    public int hashCode() {
        return x ^ y ^ z;
    }
}
