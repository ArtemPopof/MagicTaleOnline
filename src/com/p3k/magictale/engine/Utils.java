package com.p3k.magictale.engine;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.NoSuchFileException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * Various static functions.
 *
 * Created by artem96 on 04.12.16.
 */
public class Utils {

    private static final int BYTES_PER_PIXEL = 4;
    public static int loadTexture(BufferedImage image){

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    /**
     * Reads image from fs or loads from server if need so
     *
     * @param path Path of resource in file system
     * @return Buffered Image for image or IOException
     */
    public static BufferedImage loadImage(String path)
            throws IOException {
        BufferedImage image;

        try {
            File file = new File(path);
            if ( !file.exists() ) {
                throw new NoSuchFileException("No image found for given path: " + path);
            }

            image = ImageIO.read(file);
        } catch (IOException e) {
            throw e;
        }

        // TODO try to loadClient from the server

        return image;
    }

    static public BufferedImage flipHorizontally(BufferedImage rawImage) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-rawImage.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rawImage = op.filter(rawImage, null);

        return rawImage;
    }

    static public BufferedImage flipVerticaly(BufferedImage rawImage) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -rawImage.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rawImage = op.filter(rawImage, null);

        return rawImage;
    }
}
