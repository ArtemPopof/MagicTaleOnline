package server.network;

import server.ServerObject;
import server.accounts.ActiveAccounts;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by ploskov on 14.01.17.
 */
public class Broadcaster {
    private static Broadcaster broadcaster;
    private final DatagramSocket socket;
    private final ActiveAccounts activeAccounts;

    private Broadcaster() throws SocketException {
        socket = new DatagramSocket(12345);
        activeAccounts = ActiveAccounts.getInstance();
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

    public void sendObjects(Set<Map.Entry<Integer, ServerObject>> values) {
        long timestamp = System.currentTimeMillis();

        ByteBuffer buffer = ByteBuffer.allocate(values.size() * 16);
        int offset = 0;
        for (Map.Entry<Integer, ServerObject> object : values) {
            buffer.putInt(offset, object.getKey());
            buffer.putInt(offset + 4, object.getValue().getIdResMan());
            buffer.putFloat(offset + 8, object.getValue().getX());
            buffer.putFloat(offset + 12, object.getValue().getY());
            offset += 16;
        }
    }

    private void broadcastDatagram(DatagramPacket packet) {
        // удаляю "отвалившихся"
        activeAccounts.filter();

        // рассылаю оставшимся
        String[] ips = activeAccounts.getIPs();
        packet.setPort(54321);

        for (String ip : ips) {
            try {
                packet.setAddress(InetAddress.getByName(ip));

                socket.send(packet);
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
