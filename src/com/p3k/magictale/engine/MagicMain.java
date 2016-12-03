package com.p3k.magictale.engine;

import com.p3k.magictale.game.Game;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.input.Keyboard;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Main class. Contains all managers
 * and main game cycle.
 *
 * @version 0.1
 * Created by artem96 on 03.12.16.
 */
public class MagicMain {

    private Game game;

    MagicMain() {

        initDisplay();
        initGl();

        initGame();

        gameLoop();

        cleanUp();

    }


    public static void main(String[] args) {
        new MagicMain();
    }

    public void initDisplay() {

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            Keyboard.create();
            Display.setVSyncEnabled(true);
        } catch (LWJGLException ex) {
            Logger.getLogger(MagicMain.class.getName()).log(Level.SEVERE, "Something went wrong in initDisplay()");
        }
    }

    public void initGl() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_DEPTH_TEST);
        glClearColor(0,0,0,0);

    }

    private void gameLoop() {

        while (!Display.isCloseRequested()) {

            getInput();
            update();
            render();

        }
    }

    private void getInput() {
        game.processInput();
    }

    private void update() {
        game.update();
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        game.render();

        Display.update();
        Display.sync(60);

    }

    private void cleanUp () {
        Display.destroy();
        Keyboard.destroy();
    }

    private void initGame() {
        game = new Game();
    }


}
