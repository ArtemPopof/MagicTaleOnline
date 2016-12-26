package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;
import org.lwjgl.input.Mouse;


/**
 * Created by jorgen on 06.12.16.
 */

public class Window extends Widget {

    private Text title;

    public Window(Sprite sprite, Text title, MComponent parent) {
        super(parent, sprite);

        this.title = title;

        int padLeft = (this.width - title.width) / 2;
        int padTop =  (this.height / 10);

        this.title.move(padLeft, padTop);

        this.put(this.title);
    }

    @Override
    public void onMouseReleased() {

        int x = (int) (Mouse.getX() - parent.getX());
        int y = (int) (Mouse.getY() - parent.getY());

        // TODO: close on button
        /*if ( Math.abs(x - closeX) < closeSize &&
             Math.abs(y - closeY) < closeSize) {
            this.hide();
        }*/

        super.onMouseReleased();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void update() {
        super.update();
    }
}
