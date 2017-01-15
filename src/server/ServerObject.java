package server;

/**
 * Created by COMar-PC on 14.01.2017.
 */
public class ServerObject {
    private float x;
    private float y;
    private int idResMan;

    public ServerObject(int idResMan, float x, float y) throws Exception {
        this.x = x;
        this.y = y;
        this.idResMan = idResMan;
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
}
