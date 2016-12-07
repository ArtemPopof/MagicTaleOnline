package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Utils;
import javafx.scene.transform.Affine;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

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

        rawImage = flipHorizontally(rawImage);

        textureId = Utils.loadTexture(rawImage);

        this.width = width;
        this.height = height;

    }

    public Sprite(BufferedImage rawImage, float width, float height) {

        rawImage = flipHorizontally(rawImage);

        textureId = Utils.loadTexture(rawImage);

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
            //glTexCoord2f(0, 1);
            //glTexCoord2f(1, 1);
            glTexCoord2f(0, 0);

            glVertex2f(width, 0);
           // glTexCoord2f(1, 1);
           // glTexCoord2f(1, 0);
              glTexCoord2f(0, 1);

            glVertex2f(width, -height);
           // glTexCoord2f(1, 0);
           // glTexCoord2f(0, 0);
              glTexCoord2f(1, 1);

            glVertex2f(0, -height);
            //glTexCoord2f(0, 0);
            //glTexCoord2f(0, 1);
              glTexCoord2f(1, 0);



        }
        glEnd();

    }

    private BufferedImage flipHorizontally(BufferedImage rawImage) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-rawImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rawImage = op.filter(rawImage, null);

        return rawImage;
    }

    private BufferedImage flipVerticaly(BufferedImage rawImage) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -rawImage.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rawImage = op.filter(rawImage, null);

        return rawImage;
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
