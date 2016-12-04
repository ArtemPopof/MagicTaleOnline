package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Utils;
import javafx.scene.transform.Affine;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

        //TODO This method seems to me little slow,
        // if someone knows how to rotate image
        // in GL, or remake loadTexture so the
        // image will not be inversed verticaly,
        // do so.

        // read image from file
        BufferedImage rawImage = ImageIO.read(new File(texturePath));

        AffineTransform transform = new AffineTransform();

        transform.translate(rawImage.getWidth() / 2, rawImage.getHeight() / 2);
        transform.rotate(-Math.PI/2);
        transform.translate(-rawImage.getWidth() / 2, -rawImage.getHeight() / 2);

        // and convert it to another format
        BufferedImage image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();

        g.drawImage(rawImage, transform,null);

        textureId = Utils.loadTexture(image);

        this.width = width;
        this.height = height;

    }

    /**
     * This constructor should be used for sprites, that
     * not unique. If your sprite will be unique, use another
     * constructor.
     *
     * Before use this, load texture with loadTexture
     * method, to obtain textureId.
     *
     * Please, be sure that you use valid textureId,
     * otherwise your sprite might look strange.
     */

    public Sprite(int textureId, float width, float height) {

        this.textureId = textureId;
        this.width = width;
        this.height = height;

    }

    public void render() {

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

    public int getTextureId() {
        return textureId;
    }
}
