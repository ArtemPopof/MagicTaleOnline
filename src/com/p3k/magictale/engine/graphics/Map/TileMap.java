package com.p3k.magictale.engine.graphics.Map;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by COMar-PC on 14.12.2016.
 */
public class TileMap {
    // TODO Add positioning to TileMap from TileObjects
    private int xWidth = 0;
    private int yHeight = 0;
    private LinkedList<Tile> tiles = null;

    public TileMap() {
        tiles = new LinkedList<>();
    }

    public TileMap(int yHeight, int xWidth) {
        this.yHeight = yHeight;
        this.xWidth = xWidth;
        tiles = new LinkedList<>();
    }

    public LinkedList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(LinkedList<Tile> tiles) {
        this.tiles = tiles;
    }

    public boolean isPass(int layer, boolean isFly) {
        boolean isPass = true;
        if (isFly) {
            for (int i = 0; i < tiles.size(); ++i) {
                if (tiles.get(i).getTileProperties().getLayer() == layer
                        && !tiles.get(i).getTileProperties().isPass()) {
                    return false;
                }
            }
        }
        for (int i = 0; i < tiles.size(); ++i) {
            if (!tiles.get(i).getTileProperties().isPass()) {
                return false;
            }
        }
        return isPass;
    }

    public void sort() {
        Comparator<Tile> comparator = new Comparator<Tile>() {
            @Override
            public int compare(Tile o1, Tile o2) {
                if (o1.getTileProperties().getLayer() > o2.getTileProperties().getLayer()) {
                    return 1;
                }
                if (o1.getTileProperties().getLayer() < o2.getTileProperties().getLayer()) {
                    return -1;
                }
                return 0;
            }
        };
        tiles.sort(comparator);
    }

}
