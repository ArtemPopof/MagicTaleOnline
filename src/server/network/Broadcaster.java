package server.network;

import server.ServerObject;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collection;

/**
 * Created by ploskov on 14.01.17.
 */
public class Broadcaster {
    private static Broadcaster broadcaster;
    private final DatagramSocket socket;

    private Broadcaster() throws SocketException {
        socket = new DatagramSocket();
    }

    public static Broadcaster getInstance() {
        if (broadcaster == null) {
            try {
                broadcaster = new Broadcaster();
            } catch (SocketException e) {
                System.err.println("Datagram socket not created");
                System.exit(1);
            }
        }

        return broadcaster;
    }

    public void sendObjects(Collection<ServerObject> values) {

    }
}
