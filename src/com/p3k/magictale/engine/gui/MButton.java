package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.graphics.Sprite;

import static org.lwjgl.opengl.GL11.*;


/**
 * Created by jorgen on 13.12.16.
 */
public abstract class MButton extends MComponent {

    protected Sprite normalSprite, hoveredSprite, pressedSprite;

    protected enum ButtonState {
        NORMAL, HOVERED, PRESSED
    }

    protected ButtonState state = ButtonState.NORMAL;
    protected MText text = null;

    private Runnable action = null;

    /**
     * Constructor for factory
     */
    public MButton(MText text, float x, float y) {
        super(null);

        this.x = x;
        this.y = y;

        this.text = text;
    }

    // GET/SET

    public void setNormalSprite(Sprite normalSprite) {
        this.normalSprite = normalSprite;
    }

    public void setHoveredSprite(Sprite hoveredSprite) {
        this.hoveredSprite = hoveredSprite;
    }

    public void setPressedSprite(Sprite pushedSprite) {
        this.pressedSprite = pushedSprite;
    }

    public ButtonState getState() {
        return state;
    }

    public void setState(ButtonState state) {
        this.state = state;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    // OVERRIDES

    @Override
    public void update() {
        int textWidth   = text.getWidth();
        int textHeight  = text.getHeight();
        float paddingLeft    = (this.width - textWidth) / 2;
        float paddingTop  = (this.height - textHeight) / 2;

        text.move(this.x + paddingLeft, this.y - paddingTop);
    }

    @Override
    public void render() {
        Sprite currentSprite = null;

        switch (state) {
            case NORMAL:
                currentSprite = normalSprite;
                break;
            case HOVERED:
                currentSprite = hoveredSprite;
                break;
            case PRESSED:
                currentSprite = pressedSprite;
                break;
            default:
                // Mystic case
                System.err.println("CRIT: wrong button state: " + state);
                break;
        }

        if ( currentSprite == null ) {
            System.err.println("Error while render button with text " + text
                    + ": sprite is null");
            return;
        }

        glPushMatrix();
        {
            glTranslatef(x, y, 0);
            currentSprite.render();
        }
        glPopMatrix();

        // Render centered text
        text.render();
    }

    protected void onResized() {
        normalSprite.setWidth(this.width);  normalSprite.setHeight(this.height);
        hoveredSprite.setWidth(this.width); hoveredSprite.setHeight(this.height);
        pressedSprite.setWidth(this.width); pressedSprite.setHeight(this.height);
        update();
    }

    // MOUSE

    @Override
    public void onMouseOver() {
        state = (state == ButtonState.PRESSED)
                ? state : ButtonState.HOVERED;
    }

    @Override
    public void onMouseOut() {
        state = ButtonState.NORMAL;
    }

    @Override
    public void onMouseMove() {
        state = (state == ButtonState.PRESSED)
                ? state : ButtonState.HOVERED;
    }

    @Override
    public void onMousePressed() {
        state = ButtonState.PRESSED;
    }

    @Override
    public void onMouseReleased() {
        if ( action != null ) {
            action.run();
        }
        state = ButtonState.HOVERED;
    }
}
