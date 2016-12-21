package com.p3k.magictale.engine.gui;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.gui.fonts.Font;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.rmi.NoSuchObjectException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**Ø
 * Created by jorgen on 18.12.16.
 */
public class Text extends MComponent {

    private Font font;

    private String text;
    private Sprite sprite;

    private float lineHeight;
    private int lines;

    private boolean wordWrap = false;

    // This block of code need for
    // resolve strange bug. When we
    // trying to render first sprite in
    // game (before render cycle) there is
    // strange artifacts. When we render first
    // point static before every draw operation
    // all is good.
    static {
        glBegin(GL_POINT);
        glVertex2f(0, 0);
        glTexCoord2f(0, 0);
        glVertex2f(1, 0);
        glTexCoord2f(0, 1);
        glVertex2f(1, -1);
        glTexCoord2f(1, 1);
        glVertex2f(0, -1);
        glTexCoord2f(1, 0);
        glEnd();
    }

    public Text(String text, Font font) {
        this(text, font, null);
    }

    public Text(String text, Font font, MComponent parent) {
        super(parent);

        this.text = text;
        this.font = font;

        // Defaults
        this.lineHeight = 1.2f * font.getSize();
        this.lines = 1;

        createTexture();
    }

    /**
     * This function renders all text to texture,
     * so in future it can be very easy rendered
     * very quickly.
     * @apiNote this function is invoked every time text
     * is updated.
     */
    private void createTexture() {

        // Creating and binding framebuffer
        int framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

        // Creating a texture
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        // Fill texture with 0
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                getTextWidth(), getTextHeight(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // Set logical buffer of our framebuffer as texture
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        // Set logical buffer (GL_COLOR_ATTACHMENT0) to be color buffer
        IntBuffer drawBuffers = BufferUtils.createIntBuffer(1);
        drawBuffers.put(GL_COLOR_ATTACHMENT0);
        drawBuffers.rewind();

        GL20.glDrawBuffers(drawBuffers);

        if ( glCheckFramebufferStatus(GL_FRAMEBUFFER)
                != GL_FRAMEBUFFER_COMPLETE ) {
            try {
                throw new Exception("Failed to create text texture!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Render to out framebuffer!
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glViewport(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        Sprite spr = null;
        try {
            spr = new Sprite("res/bird.png", 50.0f, 0.0f);
            //spr.render();
        } catch (IOException e) {
            e.printStackTrace();
        }

        glPushMatrix();

        // This is needed because the coord center placed in top right corner
        // We need to use common OpenGL way : left bottom corner
        glTranslatef(getTextWidth() - font.getSize(), getTextHeight(), 0.0f);

        Sprite charSprite;
        // Actual rendering loop
        // TODO: multiple line rendering;
        for (int i = 0; i < text.length(); ++i) {

            try {
                charSprite = font.getSpriteFor(text.charAt(i));
                charSprite.render();
            } catch (NoSuchObjectException e) {
                System.err.println("Render text error: " + e);
            }

            // The minus here because of strange coord center
            glTranslatef(-font.getSize() - font.getSpace(),
                         0, 0);
        }
        glPopMatrix();

        // Bind screen as framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        sprite = new Sprite(texture, getTextWidth(), getTextHeight());
    }

    private int getTextWidth() {
        return (int) ( text.length() * font.getSize() +
                (text.length()) * font.getSpace()) - 5; // TODO: move magic number
    }

    private int getTextHeight() {
        return (int) lineHeight * lines;
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslatef(this.getX(), this.getY(), 0);
        sprite.render();

        glPopMatrix();
    }

    @Override
    public void update() {

    }

    @Override
    protected void onResized() {
        // Now font can't be resized
    }

    @Override
    public void onMouseOver() {

    }

    @Override
    public void onMouseOut() {

    }

    @Override
    public void onMouseMove() {

    }

    @Override
    public void onMousePressed() {

    }

    @Override
    public void onMouseReleased() {

    }

    public boolean isWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        createTexture();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getWidth() {
        return (int) sprite.getWidth();
    }

    public int getHeight() {
        return (int) sprite.getHeight();
    }
}
