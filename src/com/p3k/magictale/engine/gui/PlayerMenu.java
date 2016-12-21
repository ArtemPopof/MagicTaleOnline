package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by jorgen on 21.12.16.
 */
public class PlayerMenu extends Widget {

    // Menu padding (left and right)
    private int padding;

    // Space between menu buttons
    private int space;

    public PlayerMenu(Sprite background) {
        super(null, background);

        space = 4;
        padding = 20;

        this.width = 24 + 2 * padding;
    }

    @Override
    public void update() {

        int buttonSize = children.get(0).getWidth();
        if ( children.size() != 0 ) {
            int newWidth = children.size() * buttonSize;
            newWidth += children.size() * space;
            newWidth += 2 * padding;

            if ( (newWidth) != width ) {
                setWidth(newWidth);

                int buttonHeight = children.get(0).getHeight();
                int buttonWidth = (width + space - 2 * padding) / children.size();
                int paddingTop = (this.height - buttonHeight) / 2 + 1;
                for (int i = 0; i < children.size(); ++i) {
                    children.get(i).move(padding + buttonWidth * i, -paddingTop);
                }

            }
        }

        super.update();
    }

    @Override
    protected void onResized() {
        children.forEach(child -> {
            float dw = width / background.getWidth();
            float dh = height / background.getHeight();

            child.setHeight((int) (child.getHeight() *  dh));
        });

        super.onResized();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
