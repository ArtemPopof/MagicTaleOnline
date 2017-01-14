package client;

import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.sun.org.apache.regexp.internal.RE;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by COMar-PC on 10.01.2017.
 */
public class ClientObject {
    private int x;
    private int y;
    private int idResMan;
    private Sprite sprite;
    private long timeStamp;

    public ClientObject(int idResMan, int x, int y) throws Exception {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
