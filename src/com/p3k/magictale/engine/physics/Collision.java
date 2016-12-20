package com.p3k.magictale.engine.physics;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.GameCharacter;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.map.level.LevelManager;
import java.awt.*;

/**
 * Created by artem96 on 04.12.16.
 */
public class Collision implements Constants{

    public static boolean checkCollision(GameObject first, GameObject second) {

        Rectangle firstRect = new Rectangle((int) first.getX(), (int) first.getWidth(),
                (int) first.getY(), (int) first.getHeight());

        Rectangle secondRect = new Rectangle((int) second.getX(), (int) second.getWidth(),
                (int) second.getY(), (int) second.getHeight());


        return firstRect.intersects(secondRect);

    }

    /**
     * Check for collision between GameCharacter and
     * map or another GameCharacter
     *
     * if collision is happen, then stops player in front of tile
     */
    public static boolean checkForCollision(GameCharacter character) {

        try {
            LevelManager manager = LevelManager.getInstance();

            float x = character.getRealX();
            float y = character.getRealY();

            if (x <= 0 || x >= MAP_WIDTH * MAP_TILE_SIZE || y <= 0 || y >= MAP_HEIGHT * MAP_TILE_SIZE) {
                return true;
            }

            Point characterNextCell = LevelManager.getTilePointByCoordinates(x, y);

             return !manager.getTileMap()[characterNextCell.x][characterNextCell.y].isPass(character.getLayer(), character.isFlyable());

            /*
            if (!result) {

                Point tilePoint = LevelManager.getCoordinatesByTile(characterNextCell);

                character.setX(tilePoint.x + Constants.MAP_TILE_SIZE);
                character.setY(tilePoint.y);

                return false;
            }
             */

        } catch (Exception e) {
            System.err.println("Collision.checkForCollision(): cannot get instance of LevelManager.");
        }

        return true;
    }


    /**
     * Is given point within given rectangle
     */
    public static boolean isPointInRect(Point point, Point first, Point second) {

        if (point.x >= first.x && point.x <= second.x && point.y <= first.y && point.y >= second.y) {
            return true;
        }

        return false;
    }

}
