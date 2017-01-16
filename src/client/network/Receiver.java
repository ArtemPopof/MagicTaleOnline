package client.network;

import client.ClientGame;
import client.ClientObject;
import client.Player;
import com.p3k.magictale.engine.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ploskov on 15.01.17.
 */
public class Receiver implements Runnable {
    private static Receiver instance;
    private static Thread thread;
    private final DatagramSocket socket;
    private final ConcurrentLinkedQueue<ReceivedObject> objectsQueue;
    private final ConcurrentLinkedQueue<DatagramPacket> packets;
    private final ConcurrentLinkedQueue<Player> playerUpdates;
    private final DatagramCatcher catcher;
    private final Thread catchersThread;

    // type (byte) + timestamp (8 bytes), data (16 * 16 bytes), data length (byte), checksum (byte)
    private final int bufferSize = 1 + 8 + 16 * 16 + 2;
    private final byte[] buffer = new byte[bufferSize];

    private Receiver() throws SocketException {
        socket = new DatagramSocket(Constants.CLIENT_UDP_PORT);
        // если в течение 2 секунд не придут данные - сервер отключил нас
        socket.setSoTimeout(2000);
        objectsQueue = new ConcurrentLinkedQueue<>();
        packets = new ConcurrentLinkedQueue<>();
        playerUpdates = new ConcurrentLinkedQueue<>();

        catcher = new DatagramCatcher(socket);
        catchersThread = new Thread(catcher);
        catchersThread.start();
    }

    public static Receiver getInstance() {
        if (instance == null) {
            try {
                instance = new Receiver();
                thread = new Thread(instance);
                thread.start();
            } catch (SocketException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        return instance;
    }

    @Override
    public void run() {
        while (true) {
            if (!packets.isEmpty()) {
                DatagramPacket packet = packets.poll();

                // проверка checksum
                byte[] bytes = packet.getData();
                byte sign = bytes[0];
                for (int i = 1; i < bytes.length; i++) {
                    sign ^= bytes[i];
                }

                // если checksum корректна
                if (sign == 0) {
                    ByteBuffer data = ByteBuffer.wrap(bytes);
                    data.position(0);

                    byte type = data.get();
                    long timestamp = data.getLong();
                    switch (type) {
                        case 0:
                            byte[] objects = new byte[16 * 16];
                            data.get(objects);

                            byte count = data.get();
                            ByteBuffer objectsBuffer = ByteBuffer.wrap(objects);
                            objectsBuffer.position(0);
                            for (byte i = 0; i < count; i++) {
                                int id = objectsBuffer.getInt();
                                int spriteId = objectsBuffer.getInt();
                                float x = objectsBuffer.getFloat();
                                float y = objectsBuffer.getFloat();

                                objectsQueue.add(new ReceivedObject(id, spriteId, x, y, timestamp));
                            }
                            break;
                        case 1:
                            Player player = new Player();

                            int currentHealth = data.getInt();
                            int maxHealth = data.getInt();
                            float speed = data.getFloat();
                            int attack = data.getInt();
                            byte isDead = data.get();
                            int currentLevel = data.getInt();
                            int xpForNextLevel = data.getInt();
                            int xp = data.getInt();

                            System.out.println();
                            System.out.println(currentHealth);
                            System.out.println(maxHealth);
                            System.out.println(speed);
                            System.out.println(attack);
                            System.out.println(isDead);
                            System.out.println(currentLevel);
                            System.out.println(xpForNextLevel);
                            System.out.println(xp);

                            player.setTimestamp(timestamp);
                            player.setCurrentHealth(currentHealth);
                            player.setMaxHealth(maxHealth);
                            player.setSpeed(speed);
                            player.setAttack(attack);
                            player.setDead(isDead);
                            player.setCurrentLevel(currentLevel);
                            player.setXpForNextLevel(xpForNextLevel);
                            player.setXp(xp);
                            playerUpdates.add(player);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void tick() {
        ClientGame clientGame = (ClientGame) ClientGame.getInstance();
        while (!objectsQueue.isEmpty()) {
            ReceivedObject object = objectsQueue.poll();
            ClientObject clientObject = new ClientObject(object.getSpriteId(), object.getX(), object.getY());
            clientObject.setTimestamp(object.getTimestamp());
            clientGame.updateObject(object.getId(), clientObject);
        }
//        System.out.println(packets.size());
        while (!playerUpdates.isEmpty()) {
            Player player = playerUpdates.poll();
            clientGame.updatePlayer(player);
        }
    }

    private class ReceivedObject {
        private final int id;
        private final int spriteId;
        private final float x;
        private final float y;
        private final long timestamp;

        ReceivedObject(int id, int spriteId, float x, float y, long timestamp) {
            this.id = id;
            this.spriteId = spriteId;
            this.x = x;
            this.y = y;
            this.timestamp = timestamp;
        }

        public int getId() {
            return id;
        }

        public int getSpriteId() {
            return spriteId;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    private class DatagramCatcher implements Runnable {
        private final DatagramSocket socket;

        DatagramCatcher(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, bufferSize);
                    socket.receive(packet);
                    packets.add(packet);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    System.err.println("Server disconnect us");
                    System.exit(1);
                }
            }
        }
    }
}
