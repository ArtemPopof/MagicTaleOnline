package com.p3k.magictale.game;

import client.ClientGame;
import com.p3k.magictale.engine.graphics.GameObject;
import server.ServerGame;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractGame {
    /**
     * global {@link AbstractGame} instance
     */
    protected static AbstractGame instance;
    /**
     * container for all single-sprite objects in game
     * Integer - object id
     * GameObject - reference
     */
    private final ConcurrentHashMap<Integer, GameObject> objects;
    /**
     * game global run state
     */
    private boolean isRunning;
    /**
     * mainloop tick time between update (maybe render) actions
     */
    private long tickTimeMills;

    protected AbstractGame() {
        this.isRunning = true;
        this.tickTimeMills = 40;
        System.out.println("Server tickrate: " + 1000 / this.tickTimeMills + " t/s");

        this.objects = new ConcurrentHashMap<>();
    }

    public static AbstractGame getInstance() {
        if (instance == null) {
            throw new NullPointerException("Instance class isn't selected.");
        }

        return instance;
    }

    public static AbstractGame getServerInstance() {
        return ServerGame.getInstance();
    }

    public static AbstractGame getClientInstance() {
        return ClientGame.getInstance();
    }
}
