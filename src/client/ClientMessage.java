package client;

/**
 * Created by COMar-PC on 10.01.2017.
 */
public class ClientMessage {
    int id;
    int idResMan;
    int x;
    int y;

    public ClientMessage(int id, int idResMan, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.idResMan = idResMan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdResMan() {
        return idResMan;
    }

    public void setIdResMan(int idResMan) {
        this.idResMan = idResMan;
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
}
