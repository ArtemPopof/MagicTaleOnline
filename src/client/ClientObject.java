package client;

import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by COMar-PC on 10.01.2017.
 */
public class ClientObject {
    private float x;
    private float y;
    private int idResMan;
    private Sprite sprite;
    private long timestamp;

    public ClientObject(int idResMan, float x, float y) throws Exception {
        this.x = x;
        this.y = y;
        this.idResMan = idResMan;
        this.sprite = new Sprite(ResourceManager.getInstance().getSprite(idResMan));
    }

    public void render() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        {
            float cameraX = ((ClientGame) ClientGame.getInstance()).getCameraX();
            float cameraY = ((ClientGame) ClientGame.getInstance()).getCameraY();

            glTranslatef(this.x - cameraX, this.y - cameraY, 0);

            this.sprite.render();

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
        this.sprite = sprite;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
