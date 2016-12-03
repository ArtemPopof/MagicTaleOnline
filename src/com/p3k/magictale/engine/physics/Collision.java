package com.p3k.magictale.engine.physics;

import com.p3k.magictale.engine.graphics.GameObject;

import java.awt.*;

/**
 * Created by artem96 on 04.12.16.
 */
public class Collision {

    public static boolean checkCollision(GameObject first, GameObject second) {

        Rectangle firstRect = new Rectangle((int) first.getX(), (int) first.getWidth(),
                (int) first.getY(), (int) first.getHeight());

        Rectangle secondRect = new Rectangle((int) second.getX(), (int) second.getWidth(),
                (int) second.getY(), (int) second.getHeight());


        return firstRect.intersects(secondRect);

    }

}
