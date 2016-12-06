package com.p3k.magictale.engine;

import com.p3k.magictale.engine.sound.SoundManager;
import com.p3k.magictale.game.Game;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Main class. Contains all managers
 * and main game cycle.
 *
 * @version 0.1
 *          Created by artem96 on 03.12.16.
 */
public class MagicMain {

    private Game game;

    private boolean isInFullScreenMode = true;

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
            if (isInFullScreenMode) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                DisplayMode max = modes[0];

                for (DisplayMode mode : modes) {
                    System.out.println("test mode: " + mode);
                    if (mode.getWidth() * mode.getHeight() * mode.getFrequency() >
                            max.getWidth() * max.getHeight() * max.getFrequency()) {
                        max = mode;
                    }
                }

                System.out.println("choose mode: " + max);
                Display.setDisplayModeAndFullscreen(max);
            } else {
                Display.setDisplayMode(new DisplayMode(800, 600));
            }

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
        glClearColor(0, 0, 0, 0);

        glEnable(GL_TEXTURE_2D);


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

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            game.cleanUp();
        }
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

    private void cleanUp() {
        Display.destroy();
        Keyboard.destroy();
        SoundManager.destroy();
    }

    private void initGame() {
        game = new Game();
    }


}
