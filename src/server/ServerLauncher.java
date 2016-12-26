package server;

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
        ServerGame server = (ServerGame) ServerGame.getInstance();
        server.mainLoop();
    }
}

