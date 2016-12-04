package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Utils;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by artem96 on 03.12.16.
 */
public class Sprite {

    private float r, g, b;

    private float width;
    private float height;

    private int textureId;


    public Sprite(float r, float g, float b, float width, float height) {
        this.r = r;
        this.g = g;
        this.b = b;

        this.width = width;
        this.height = height;

        textureId = -1;
    }

    // Don't use this constructor, if there are more
    // objects of that type
    //
    public Sprite(String texturePath, float width, float height) throws IOException {

        BufferedImage test = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = test.createGraphics();

        g2d.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        g2d.fillRect(0, 0, 128, 128); //A transparent white background

        g2d.setColor(Color.red);
        g2d.drawRect(0, 0, 127, 127); //A red frame around the image
        g2d.fillRect(10, 10, 10, 10); //A red box

        g2d.setColor(Color.blue);
        g2d.drawString("Test image", 10, 64); //Some blue text

        textureId = Utils.loadTexture(test);

    }

    public void render() {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (textureId == -1)
            glColor3f(r, g, b);
        else
            glBindTexture(GL_TEXTURE_2D, textureId);

        glBegin(GL_QUADS); {

            glVertex2f(0, 0);
            glTexCoord2f(0, 0);

            glVertex2f(0, height);
            glTexCoord2f(0, 1);


            glVertex2f(width, height);
            glTexCoord2f(1, 1);

            glVertex2f(width, 0);
            glTexCoord2f(1, 0);


        }
        glEnd();

    }


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
