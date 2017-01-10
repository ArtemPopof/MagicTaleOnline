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
    protected static AbstractGame instanceServer;
    /**
     * container for all single-sprite objects in game
     * Integer - object id
     * GameObject - reference
     */
    protected final ConcurrentHashMap<Integer, GameObject> objects;
    /**
     * mainloop tick time between update (maybe render) actions
     */
    private final long tickTimeMills;
    /**
     * game global run state
     */
    private boolean isRunning;

    /**
     * {@link AbstractGame} constructor
     * which define tickRate of server
     * and set server status to running
     */
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

    /**
     * главный цикл игры (что на сервере, что на клиенте)
     * с постоянной паузой
     */
    public final void mainLoop() {
        // сразу заходим в цикл
        long lastRunMillis = System.currentTimeMillis() - this.tickTimeMills;
        while (this.isRunning) {
            if (System.currentTimeMillis() - lastRunMillis < this.tickTimeMills) {
                continue;
            }
            lastRunMillis += this.tickTimeMills;

            tick();
        }
    }

    /**
     * один tick в игре, на сервере и клиенте должен иметь свои обработчики
     */
    public abstract void tick();
}
