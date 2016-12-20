package com.p3k.magictale.game.Characters;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.algorithms.AStarFindAlgorithm;
import com.p3k.magictale.engine.enums.Direction;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.physics.Collision;
import com.p3k.magictale.game.Game;
import com.p3k.magictale.game.GameObjects;
import com.p3k.magictale.map.level.LevelManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Just player class with auto-pilot mode =)
 *
 *
 * Created by artem96 on 12.12.16.
 */
public class Bot extends GameCharacter {

    /**
     * Bot states
     */

    /**
     * Just waiting a waitTime period of time
     */
    private static final int BOT_WAITING_STATE = 0;

    /**
     * nothing to do, so move to the random cell in your
     * vision radius
     */
    private static final int BOT_PATRULING_STATE = 1;

    /**
     * Bot has the target, so find a way to destroy it
     */
    private static final int BOT_TARGETSPOTED_STATE = 2;


    private int currentBotState;

    /**
     * How many keyboard keys will be emulated
     *
     */
    private static final int EMULATED_KEYS = 10;

    /**
     * Keys for emulation of pressing
     */
    private static final int KEY_W = 0;
    private static final int KEY_S = 1;
    private static final int KEY_A = 2;
    private static final int KEY_D = 3;

    private static final int KEY_SPACE = 4;

    private boolean[] keysState;

    // bot can see you if you
    // in this imaginary circle
    // units is tiles
    private int visionRadius;

    // how far bot can see in
    // cells
    private int visionCellRadius;

    /**
     * If not aggressive, then bot
     * will be attack you only if
     * you trying to hurt him.
     *
     */
    private boolean isAggressive;

    /**
     * Just a random generator
     */
    private Random random;

    /**
     * Patruling mode variables
     */

    private float destinationX;
    private float destinationY;

    /**
     * path to target
     */
    private ArrayList<Point> pathToTarget;

    /**
     * Next cell to go
     */
    private int nextCellInPath;

    /**
     * Waiting mode variables
     */

    private int framesToWait;


    //TODO remove nah
    //TEMPORARY OBJECT
    private boolean[][] field;

    //Spotted enemy
    GameCharacter spottedEnemy ;


    /**
     *
     * Basic constructor for bot, almost like GameCharacter one
     *
     * @param x x coord of bot
     * @param y y coord of bot
     * @param width width of bot
     * @param height height of bot
     */


    public Bot(float x, float y, float width, float height) {
        super(x, y, width, height);

        // get player animations
        type = CharacterTypes.ABSTRACT_BOT;

        try {
            animations = ResourceManager.getInstance().getAnimations(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 10 keys should be enough, i think
        // all values equal false?
        keysState = new boolean[EMULATED_KEYS];

        // how many pixels is visible to bot
        visionCellRadius = 10;
        visionRadius = visionCellRadius * Constants.TILE_SIZE;

        isAggressive = true;

        currentBotState = BOT_PATRULING_STATE;

        random = new Random(System.currentTimeMillis());

        destinationX = getRealX();
        destinationY = getRealY();

        pathToTarget = null;
        nextCellInPath = -1;

        framesToWait = -1;

        // TEMP CODE NEXT

        field = new boolean[Constants.MAP_WIDTH][Constants.MAP_HEIGHT];

        // init temp map with all passable values
        // bot can move to every location.
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                try {
                    field[j][i] = LevelManager.getInstance().getTileMap()[j][i].isPass(1, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Emulation of real-player
     * input reaction. Bot simulating
     * key press with boolean global
     * variables like isKeyDown(int KEY)
     */
    public void processInput() {

        try {

            if (isDead()) {
                return;
            }

            boolean isSomethingHappens = false;

            if (isKeyDown(KEY_W)) {
                changeState(UP_MOVE_STATE);
                move(0, 1);
                setDirection(Direction.UP);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_S)) {
                changeState(DOWN_MOVE_STATE);
                move(0, -1);
                setDirection(Direction.DOWN);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_A)) {
                changeState(LEFT_MOVE_STATE);
                move(-1, 0);
                setDirection(Direction.LEFT);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_D)) {
                changeState(RIGHT_MOVE_STATE);
                move(1, 0);
                setDirection(Direction.RIGHT);
                isSomethingHappens = true;
            }
            if (isKeyDown(KEY_SPACE)){
                // nothing's here yet
            }

            if (!isSomethingHappens) {
                animations.get(getState()).pause();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {

        super.update();


        if (isDead()) {
            return;
        }

        // here will be simulation of bot virtual brain

        if (spottedEnemy==null && isAggressive && isAnyoneNotFriendlyAround()) {
            setBotState(BOT_TARGETSPOTED_STATE);
        }

        if (currentBotState == BOT_PATRULING_STATE) {
            doPatroling();
        }
        if (currentBotState == BOT_WAITING_STATE) {
            boolean isEnoughWaiting = doWait();

            if (isEnoughWaiting) {
                setBotState(BOT_PATRULING_STATE);
            }
        }
        if (currentBotState == BOT_TARGETSPOTED_STATE);

    }

    /**
     * Just walk around the current spot in
     * random manner
     */
    private void doPatroling() {

        if (destinationY == -1) {
            // state has been changed just now
            // so generate random destination to follow

            int randomX = random.nextInt( visionRadius * 2);
            int randomY = random.nextInt( visionRadius * 2);

            randomX = (-1 * visionRadius) + randomX;

            randomY = (-1 * visionRadius) + randomY;

            // if bot want to go to current cell
            if (Math.abs(randomX) <= Constants.TILE_SIZE && Math.abs(randomY) <= Constants.TILE_SIZE) {
                randomX += Constants.TILE_SIZE + 1;
            }

            destinationY = getRealY() + randomY;
            destinationX = getRealX() + randomX;

            //here will be collision check, so we can be
            //sure that destination is reachable

        }else if ((getRealX() - destinationX) <= 5 && (getRealY() - destinationY) <= 5) {
            // if bot come pretty close to it's destination point
            setBotState(BOT_WAITING_STATE);
            destinationX = -1;
            destinationY = -1;
            nextCellInPath = -1;

            clearKeyStates();
        } else if (nextCellInPath == -1) {

            // use a brain, to generate path to target
            // a* algorithm?

            Point start = LevelManager.getTilePointByCoordinates(getRealX(), getRealY());
            Point goal  = LevelManager.getTilePointByCoordinates(destinationX, destinationY);

            pathToTarget = AStarFindAlgorithm.findPath(
                    field, Constants.MAP_WIDTH, Constants.MAP_HEIGHT, start, goal);

            // cannot go where bot wants to go =(
            if (pathToTarget == null) {
                setBotState(BOT_WAITING_STATE);
                destinationX = -1;
                destinationY = -1;
                nextCellInPath = -1;
                return;
            }

            nextCellInPath = 0;

        } else {

            // so we know the path and can go to target
            Point currentTile = LevelManager.getTilePointByCoordinates(getRealX(), getRealY());
            Point nextTile = pathToTarget.get(nextCellInPath);

            // reset all keys
            clearKeyStates();

            if (currentTile.equals(nextTile)) {

                // destination arrived
                if (nextCellInPath == pathToTarget.size()-1) {
                    setBotState(BOT_WAITING_STATE);
                    destinationX = -1;
                    destinationY = -1;
                    nextCellInPath = -1;
                    return;
                }

                nextCellInPath++;
                return;
            }

            if (nextTile.x < currentTile.x) {
                emulatekey(KEY_A);
            } else if (nextTile.x > currentTile.x) {
                emulatekey(KEY_D);
            }

            if (nextTile.y < currentTile.y) {
                emulatekey(KEY_W);
            } else if (nextTile.y > currentTile.y) {
                emulatekey(KEY_S);
            }
        }

    }

    /**
     * Waiting another frame. And decrement waitFrames variable.
     * If it's = zero, then return true.
     */
    private boolean doWait() {

        // if it's first frame to wait
        // so decide, how long he's going to wait
        if (framesToWait == -1) {
            framesToWait = random.nextInt(5 * Constants.MAX_FPS);
        } else if (framesToWait > 0) {
            // waiting another frame
            framesToWait--;
        } else {
            // enough waiting
            setBotState(BOT_PATRULING_STATE);
            framesToWait = -1;
            return true;
        }

        return false;
    }



    /**
     * Check if virtual bot pressed the given key
     *
     * @param keyCode - key being checked
     * @return - true or false
     * @throws IllegalArgumentException - if keyCode is incorrect
     */
    private boolean isKeyDown(int keyCode) throws IllegalArgumentException {

        if (keyCode < 0 || keyCode >= EMULATED_KEYS) {
            throw new IllegalArgumentException("isKeyDown: unknown keyCode");
        }
        return keysState[keyCode];
    }

    /**
     * Simulate key pressed by human.
     * But it is bot's brain work
     *
     * @param keyCode - key to be simulated
     */
    private void emulatekey(int keyCode) {
        if (keyCode < 0 || keyCode >= EMULATED_KEYS) {
            System.err.print("Bot.emulateKey("+keyCode+"): invalid argument");
        } else {
            keysState[keyCode] = true;
        }
    }

    /**
     * Set bot state
     */

    private void setBotState(int state) {
        currentBotState = state;
    }

    /**
     * Clear all key states
     */
    private void clearKeyStates() {
        for (int i = 0; i < keysState.length; i++) {
            keysState[i] = false;
        }
    }

    /**
     * Is there are some enemies in view radius of this bot
     */

    public boolean isAnyoneNotFriendlyAround() {

        Point currentCell = LevelManager.getTilePointByCoordinates(this.getRealX(), this.getRealY());

        // Bot's vision arean
        Point firstRectCell = new Point();
        Point secondRectCell = new Point();


        // CAUTION! All coordinates is supposed to be in openGL system (0.0 in bottom left corner);


        switch (direction) {

            case UP:
                firstRectCell.x = currentCell.x - visionCellRadius;
                firstRectCell.y = currentCell.y;
                secondRectCell.x = currentCell.x + visionCellRadius;
                secondRectCell.y = currentCell.y + visionCellRadius;
                break;
            case DOWN:
                firstRectCell.x = currentCell.x - visionCellRadius;
                firstRectCell.y = currentCell.y;
                secondRectCell.x = currentCell.x + visionCellRadius;
                secondRectCell.y = currentCell.y - visionCellRadius;
                break;
            case LEFT:
                firstRectCell.x = currentCell.x - visionCellRadius;
                firstRectCell.y = currentCell.y + visionCellRadius;
                secondRectCell.x = currentCell.x;
                secondRectCell.y = currentCell.y - visionCellRadius;
                break;
            case RIGHT:
                firstRectCell.x = currentCell.x;
                firstRectCell.y = currentCell.y + visionCellRadius;
                secondRectCell.x = currentCell.x + visionCellRadius;
                secondRectCell.y = currentCell.y - visionCellRadius;
                break;
            default:
                break;
        }

        GameObjects objects = Game.getInstance().getObjects();

        for (int i = 0; i < objects.size(); i++) {

            GameObject object = objects.get(i);

            if (GameCharacter.class.isInstance(object)) {
                GameCharacter character = (GameCharacter) object;

                if (character.equals(this)) {
                    continue;
                }

                Point characterCell = LevelManager.
                        getTilePointByCoordinates(character.getRealX(), character.getRealY());

                if (Collision.isPointInRect(characterCell, firstRectCell, secondRectCell)) {
                    // found the enemy
                    spottedEnemy = character;
                    System.out.println("FOUND ENEMY: "+ character.toString());

                    return true;
                }

            }
        }

        this.isAggressive = true;

        return false;
    }
}
