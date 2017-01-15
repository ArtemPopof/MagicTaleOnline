package com.p3k.magictale.engine;

import client.ClientGame;
import com.p3k.magictale.engine.sound.SoundManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import server.ServerGame;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Main class. Contains all managers
 * and main clientGame cycle.
 *
 * @version 0.1
 *          Created by artem96 on 03.12.16.
 */
public class MagicMain {

    private ClientGame clientGame;
    private ServerGame serverGame;

    private boolean isRunning = true;

    MagicMain() {

        initGame();

        gameLoop();

    }


    public static void main(String[] args) {
        new MagicMain();
    }

    private void gameLoop() {

        long timer = System.currentTimeMillis();
        long step = 1000 / 40;
        while (this.isRunning && !Display.isCloseRequested()) {

            if (System.currentTimeMillis() - timer > step) {
                getInput();
                update();
                timer += step;
            }
            render();

        }
    }

    private void getInput() {
        this.clientGame.processInput();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.isRunning = false;
        }
    }

    private void update() {
        this.clientGame.update();
    }

    private void render() {



        this.clientGame.render();



    }


    private void initGame() {
        this.serverGame = (ServerGame) ServerGame.getServerInstance();
        this.clientGame = (ClientGame) ClientGame.getClientInstance();
    }


}
