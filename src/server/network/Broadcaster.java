package server.network;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.game.Characters.Player;
import server.ServerObject;
import server.accounts.ActiveAccounts;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

/**
 * Created by ploskov on 14.01.17.
 */
public class Broadcaster {
    private static Broadcaster broadcaster;
    private final DatagramSocket socket;
    private final ActiveAccounts activeAccounts;

    private final int dataSize = 16 * 16;
    // type (byte), timestamp (8 bytes), data (16 * 16 bytes), data length (byte), checksum (byte)
    private final int datagramSize = 1 + 8 + dataSize + 2;

    private Broadcaster() throws SocketException {
        socket = new DatagramSocket(Constants.SERVER_UDP_PORT);
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

    public void sendPlayerStatus(Player player, String ip) {
        // тип "обновление статуса игрока"
        byte type = 1;
        long timestamp = System.currentTimeMillis();

        byte[] datagram = new byte[datagramSize];
        ByteBuffer datagramBuffer = ByteBuffer.wrap(datagram);

        int currentHealth = player.getCurrentHealth();
        int maxHealth = player.getMaxHealth();
        float speed = player.getSpeed();
        int attack = player.getAttack();
        byte isDead = (byte) (player.isDead() ? 1 : 0);
        int currentLevel = player.getLevel();
        int xpForNextLevel = player.getExperienceForNextLevel();
        int xp = player.getXp();

        datagramBuffer.put(type);
        datagramBuffer.putLong(timestamp);
        datagramBuffer.putInt(currentHealth);
        datagramBuffer.putInt(maxHealth);
        datagramBuffer.putFloat(speed);
        datagramBuffer.putInt(attack);
        datagramBuffer.put(isDead);
        datagramBuffer.putInt(currentLevel);
        datagramBuffer.putInt(xpForNextLevel);
        datagramBuffer.putInt(xp);

//        System.out.println();
//        System.out.println(currentHealth);
//        System.out.println(maxHealth);
//        System.out.println(speed);
//        System.out.println(attack);
//        System.out.println(isDead);
//        System.out.println(currentLevel);
//        System.out.println(xpForNextLevel);
//        System.out.println(xp);

        // подписываем датаграмму
        byte sign = datagram[0];
        for (int j = 1; j < datagramSize - 1; j++) {
            sign ^= datagram[j];
        }
        datagram[datagramSize - 1] = sign;

        // создаём DatagramPacket
        DatagramPacket packet = new DatagramPacket(datagram, datagramSize);
        try {
            packet.setAddress(InetAddress.getByName(ip));
            packet.setPort(Constants.CLIENT_UDP_PORT);

            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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

        byte[] bytes = buffer.array();
        // упаковываем до 16 объектов в датаграмму
        for (int i = 0; i < bytes.length; i += dataSize) {
            int length = Math.min(dataSize, bytes.length - i);

            byte[] datagram = new byte[datagramSize];

            ByteBuffer datagramBuffer = ByteBuffer.wrap(datagram);
            // type == 0 -> objects update
            datagramBuffer.put((byte) 0);
            datagramBuffer.putLong(timestamp);
            datagramBuffer.put(bytes, i, length);
            datagramBuffer.put((byte) (length / 16));

            // подписываем датаграмму
            byte sign = datagram[0];
            for (int j = 1; j < datagramSize - 1; j++) {
                sign ^= datagram[j];
            }
            datagram[datagramSize - 1] = sign;

            // создаём DatagramPacket
            DatagramPacket packet = new DatagramPacket(datagram, datagramSize);
            broadcastDatagram(packet);
        }
    }

    private void broadcastDatagram(DatagramPacket packet) {
        // удаляю "отвалившихся"
        activeAccounts.filter();

        // рассылаю оставшимся
        String[] ips = activeAccounts.getIPs();
//        System.out.println("broadcast diagram to " + ips.length + " clients");

        for (String ip : ips) {
            try {
                packet.setAddress(InetAddress.getByName(ip));
                packet.setPort(Constants.CLIENT_UDP_PORT);

                socket.send(packet);
//                System.out.println("Send packet " + packet.getAddress().getHostAddress() + " " + packet.getPort());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
