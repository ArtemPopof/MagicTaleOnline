package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.GameCharacter;
import server.ServerGame;

/**
 * Character representation for server
 * No animations, no render and so on
 *
 * Created by artem96 on 16.01.17.
 */
public class ServerCharacter extends GameCharacter{

    public ServerCharacter(float x, float y, float width, float height) {
        super(x, y, width, height, false);

    }

    public void render() {
        // do nothing, becouse it's a server
    }

    public void update() {
        // sync with server

        System.out.println("PUSHING WITH ID: " + getId());

        ((ServerGame) ServerGame.getInstance()).putServerObject
                (getId(), animations.get(getState()).getCurrentFrame().getSpriteId(), this.x, this.y);

    }
}
