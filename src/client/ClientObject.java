package client;

import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by COMar-PC on 10.01.2017.
 */
public class ClientObject {
    private float x;
    private float y;
    private int idResMan;
    private Sprite sprite;
    private long timestamp;
    private Sprite second;

    public ClientObject(int idResMan, float x, float y) {
        this.x = x;
        this.y = y;
//        System.out.println("IdResMan = " + idResMan);
        this.idResMan = idResMan;
        this.sprite = ResourceManager.getInstance(true).getSprite(idResMan);
        if (sprite == null) {
            sprite = new Sprite(0, 0, 0, 10, 10);
        }
        second = new Sprite(0, 0, 255, 3, 3);
    }

    public void render() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            float cameraX = ((ClientGame) ClientGame.getInstance()).getCameraX() + sprite.getWidth() / 2;
            float cameraY = ((ClientGame) ClientGame.getInstance()).getCameraY() - sprite.getHeight();

            glTranslatef(this.x - cameraX, this.y - cameraY, 0);

            this.sprite.render();
//            this.second.render();

        }
        glPopMatrix();
    }

    public void renderPoint() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            float cameraX = ((ClientGame) ClientGame.getInstance()).getCameraX();
            float cameraY = ((ClientGame) ClientGame.getInstance()).getCameraY();

            glTranslatef(this.x - cameraX, this.y - cameraY, 0);

//            this.sprite.render();
            this.second.render();

        }
        glPopMatrix();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getIdResMan() {
        return idResMan;
    }

    public void setIdResMan(int idResMan) {
        this.idResMan = idResMan;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        if (sprite != null) {
            this.sprite = sprite;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
