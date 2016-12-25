package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Aleksandr Ploskov
 */
public class ServerLauncher {
    /**
     * bind rmi to port 1099
     * bind rmi name ".../MagicTaleOnline"
     * <p>
     * starts server mainloop
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            String rmiName = "MagicTaleOnline";

            GameServer server = GameServer.getInstance();
            Naming.bind(rmiName, server);
            server.mainLoop();
        } catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

