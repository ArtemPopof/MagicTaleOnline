package com.p3k.magictale.game;

import com.p3k.magictale.engine.graphics.GameObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For server syncronization
 *
 * Created by artem96 on 20.12.16.
 */
public class GameObjects {

    ArrayList<GameObject> objects;

    public GameObjects() {
        objects = new ArrayList<>();
    }

    public GameObject get(int index) {
        return objects.get(index);
    }

    public void set(GameObject object, int index) {
        this.objects.set(index, object);
    }

    public int size() {
        return objects.size();
    }

    public void add(GameObject object) {
        objects.add(object);
    }


}
